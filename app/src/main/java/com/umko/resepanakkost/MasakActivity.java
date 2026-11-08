package com.umko.resepanakkost;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Mode memasak: langkah ditampilkan satu per satu dengan timer.
 * Posisi langkah dan timer hanya berlaku selama layar ini terbuka,
 * sengaja tidak disimpan di SharedPreferences maupun Room.
 */
public class MasakActivity extends AppCompatActivity {

    private final List<String> langkah = new ArrayList<>();
    private int index = 0;
    private int urutanReaksi = 0;
    private int menit = 0;
    private boolean timerJalan = false;
    private CountDownTimer timer;

    private ImageView ivAyam;
    private TextView tvTimer, tvPosisi, tvLangkah;
    private MaterialButton btnTimer, btnKembali, btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masak);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        ivAyam = findViewById(R.id.ivAyam);
        tvTimer = findViewById(R.id.tvTimer);
        tvPosisi = findViewById(R.id.tvPosisi);
        tvLangkah = findViewById(R.id.tvLangkah);
        btnTimer = findViewById(R.id.btnTimer);
        btnKembali = findViewById(R.id.btnKembali);
        btnLanjut = findViewById(R.id.btnLanjut);

        Anim.pop(ivAyam);
        ivAyam.setOnClickListener(v -> Anim.reaksi(v, urutanReaksi++));
        Anim.efekTekan(btnLanjut);
        Anim.efekTekan(btnKembali);

        btnTimer.setOnClickListener(v -> {
            if (timerJalan) {
                timer.cancel();
                timerJalan = false;
                tvTimer.setText(format(menit * 60L));
                btnTimer.setText("Mulai timer");
            } else {
                mulaiTimer();
            }
        });

        btnKembali.setOnClickListener(v -> {
            if (index > 0) {
                index--;
                tampilLangkah();
            }
        });
        btnLanjut.setOnClickListener(v -> {
            if (index >= langkah.size()) {
                finish();
            } else {
                index++;
                tampilLangkah();
            }
        });

        final int id = getIntent().getIntExtra("resep_id", -1);
        AppDatabase.IO.execute(() -> {
            Resep r = AppDatabase.get(this).resepDao().cari(id);
            runOnUiThread(() -> {
                if (r == null) {
                    finish();
                    return;
                }
                toolbar.setTitle(r.nama);
                menit = r.menit;
                for (String s : r.langkah.split("\n")) {
                    if (!s.trim().isEmpty()) langkah.add(s.trim());
                }
                tvTimer.setText(format(menit * 60L));
                tampilLangkah();
            });
        });
    }

    private void tampilLangkah() {
        if (index < langkah.size()) {
            tvPosisi.setText("Langkah " + (index + 1) + " dari " + langkah.size());
            tvLangkah.setText(langkah.get(index));
            btnLanjut.setText(index == langkah.size() - 1 ? "Selesai" : "Lanjut");
        } else {
            tvPosisi.setText("Selesai");
            tvLangkah.setText("Masakan sudah siap. Selamat makan!");
            btnLanjut.setText("Tutup");
            Anim.putar(ivAyam);
        }
        btnKembali.setEnabled(index > 0);
        Anim.muncul(tvLangkah, 0);
        if (index < langkah.size()) Anim.reaksi(ivAyam, urutanReaksi++);
    }

    private void mulaiTimer() {
        timer = new CountDownTimer(menit * 60000L, 1000) {
            @Override
            public void onTick(long ms) {
                tvTimer.setText(format(ms / 1000));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                timerJalan = false;
                btnTimer.setText("Mulai timer");
                Anim.goyang(ivAyam);
                Toast.makeText(MasakActivity.this, "Waktu memasak habis",
                        Toast.LENGTH_LONG).show();
            }
        }.start();
        timerJalan = true;
        btnTimer.setText("Berhenti");
    }

    private String format(long detik) {
        return String.format(Locale.US, "%02d:%02d", detik / 60, detik % 60);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
