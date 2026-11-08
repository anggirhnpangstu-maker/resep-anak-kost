package com.umko.resepanakkost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class DetailResepActivity extends AppCompatActivity {

    private ResepDao dao;
    private Resep resep;
    private List<Bahan> bahan;
    private int porsi;

    private TextView tvNama, tvInfo, tvPorsi, tvTotal, tvLangkah;
    private LinearLayout llBahan;
    private View btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvNama = findViewById(R.id.tvNama);
        tvInfo = findViewById(R.id.tvInfo);
        tvPorsi = findViewById(R.id.tvPorsi);
        tvTotal = findViewById(R.id.tvTotal);
        tvLangkah = findViewById(R.id.tvLangkah);
        llBahan = findViewById(R.id.llBahan);
        btnHapus = findViewById(R.id.btnHapus);
        View btnKurang = findViewById(R.id.btnKurang);
        View btnTambah = findViewById(R.id.btnTambah);
        View btnMasak = findViewById(R.id.btnMasak);

        dao = AppDatabase.get(this).resepDao();
        // Jumlah porsi awal diambil dari SharedPreferences
        porsi = new PrefsManager(this).ambilPorsiBawaan();
        final int id = getIntent().getIntExtra("resep_id", -1);

        btnKurang.setOnClickListener(v -> {
            if (porsi > 1) {
                porsi--;
                isiPorsi();
                Anim.pop(tvPorsi);
            }
        });
        btnTambah.setOnClickListener(v -> {
            if (porsi < 20) {
                porsi++;
                isiPorsi();
                Anim.pop(tvPorsi);
            }
        });

        Anim.efekTekan(btnMasak);
        btnMasak.setOnClickListener(v -> {
            Intent i = new Intent(this, MasakActivity.class);
            i.putExtra("resep_id", resep.id);
            i.putExtra("porsi", porsi);
            startActivity(i);
        });

        btnHapus.setOnClickListener(v -> new MaterialAlertDialogBuilder(this)
                .setTitle("Hapus resep")
                .setMessage("Resep ini akan dihapus beserta bahannya.")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus", (d, w) -> AppDatabase.IO.execute(() -> {
                    dao.hapus(resep);
                    runOnUiThread(this::finish);
                }))
                .show());

        AppDatabase.IO.execute(() -> {
            Resep r = dao.cari(id);
            List<Bahan> b = r == null ? null : dao.bahanDari(id);
            runOnUiThread(() -> {
                if (r == null) {
                    finish();
                    return;
                }
                resep = r;
                bahan = b;
                tampil();
            });
        });
    }

    private void tampil() {
        tvNama.setText(resep.nama);
        tvInfo.setText(Rupiah.format(resep.biaya) + " per porsi - " + resep.menit + " menit");

        String[] baris = resep.langkah.split("\n");
        StringBuilder sb = new StringBuilder();
        int no = 1;
        for (String s : baris) {
            if (s.trim().isEmpty()) continue;
            if (no > 1) sb.append("\n");
            sb.append(no++).append(". ").append(s.trim());
        }
        tvLangkah.setText(sb.toString());

        btnHapus.setVisibility(resep.bawaan ? View.GONE : View.VISIBLE);
        isiPorsi();
        Anim.muncul(tvNama, 0);
    }

    /** Takaran bahan dikalikan jumlah porsi. */
    private void isiPorsi() {
        tvPorsi.setText(porsi + " porsi");
        tvTotal.setText("Perkiraan biaya: " + Rupiah.format(resep.biaya * porsi));

        llBahan.removeAllViews();
        for (Bahan b : bahan) {
            TextView tv = new TextView(this);
            String satuan = b.satuan == null || b.satuan.isEmpty() ? "" : " " + b.satuan;
            tv.setText(Fmt.jumlah(b.jumlah * porsi) + satuan + " " + b.nama);
            tv.setTextSize(15f);
            tv.setPadding(0, 6, 0, 6);
            llBahan.addView(tv);
        }
    }
}
