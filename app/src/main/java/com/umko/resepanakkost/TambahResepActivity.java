package com.umko.resepanakkost;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/** Fitur tambahan: pengguna menulis resep sendiri. */
public class TambahResepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText etNama = findViewById(R.id.etNama);
        TextInputEditText etBiaya = findViewById(R.id.etBiaya);
        TextInputEditText etMenit = findViewById(R.id.etMenit);
        TextInputEditText etBahan = findViewById(R.id.etBahan);
        TextInputEditText etLangkah = findViewById(R.id.etLangkah);
        View btnSimpan = findViewById(R.id.btnSimpan);

        Anim.efekTekan(btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            String nama = String.valueOf(etNama.getText()).trim();
            if (nama.isEmpty()) {
                etNama.setError("Isi nama masakan");
                return;
            }

            long biaya;
            int menit;
            try {
                biaya = Long.parseLong(String.valueOf(etBiaya.getText()).trim());
            } catch (NumberFormatException e) {
                etBiaya.setError("Isi biaya per porsi");
                return;
            }
            try {
                menit = Integer.parseInt(String.valueOf(etMenit.getText()).trim());
            } catch (NumberFormatException e) {
                etMenit.setError("Isi lama masak");
                return;
            }
            if (menit < 1) {
                etMenit.setError("Minimal 1 menit");
                return;
            }

            String langkah = String.valueOf(etLangkah.getText()).trim();
            if (langkah.isEmpty()) {
                etLangkah.setError("Isi minimal satu langkah");
                return;
            }

            // Setiap baris bahan: nama, jumlah, satuan
            List<String[]> bahanMentah = new ArrayList<>();
            for (String baris : String.valueOf(etBahan.getText()).split("\n")) {
                if (baris.trim().isEmpty()) continue;
                String[] p = baris.split(",");
                String bn = p[0].trim().toLowerCase();
                double jumlah = 1;
                String satuan = "";
                try {
                    if (p.length > 1) jumlah = Double.parseDouble(p[1].trim());
                } catch (NumberFormatException e) {
                    etBahan.setError("Jumlah harus angka: " + baris.trim());
                    return;
                }
                if (p.length > 2) satuan = p[2].trim();
                bahanMentah.add(new String[]{bn, String.valueOf(jumlah), satuan});
            }
            if (bahanMentah.isEmpty()) {
                etBahan.setError("Isi minimal satu bahan");
                return;
            }

            final long biayaFinal = biaya;
            final int menitFinal = menit;
            AppDatabase.IO.execute(() -> {
                ResepDao dao = AppDatabase.get(this).resepDao();
                long id = dao.tambahResep(new Resep(nama, biayaFinal, menitFinal, langkah, false));
                List<Bahan> daftar = new ArrayList<>();
                for (String[] b : bahanMentah) {
                    daftar.add(new Bahan((int) id, b[0], Double.parseDouble(b[1]), b[2]));
                }
                dao.tambahBahan(daftar);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Resep tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
