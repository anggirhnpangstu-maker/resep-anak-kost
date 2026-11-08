package com.umko.resepanakkost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class PengaturanFragment extends Fragment {

    private PrefsManager prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pengaturan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = new PrefsManager(requireContext());

        ImageView ivHeader = view.findViewById(R.id.ivHeader);
        TextInputEditText etUsername = view.findViewById(R.id.etUsername);
        TextInputEditText etPorsi = view.findViewById(R.id.etPorsi);
        TextInputEditText etBatas = view.findViewById(R.id.etBatas);
        SwitchMaterial switchTema = view.findViewById(R.id.switchTema);
        View btnSimpan = view.findViewById(R.id.btnSimpan);

        // Nilai tersimpan dibaca dari SharedPreferences
        etUsername.setText(prefs.ambilUsername());
        etPorsi.setText(String.valueOf(prefs.ambilPorsiBawaan()));
        long batas = prefs.ambilBatasBiaya();
        if (batas > 0) etBatas.setText(String.valueOf(batas));
        switchTema.setChecked(prefs.isTemaGelap());

        // Menu Pengaturan: roda gigi berputar saat dibuka dan saat disentuh
        Anim.putar(ivHeader);
        ivHeader.setOnClickListener(Anim::putar);
        Anim.efekTekan(btnSimpan);

        switchTema.setOnCheckedChangeListener((btn, gelap) -> {
            prefs.simpanTema(gelap);
            AppCompatDelegate.setDefaultNightMode(gelap
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnSimpan.setOnClickListener(v -> {
            int porsi;
            try {
                porsi = Integer.parseInt(String.valueOf(etPorsi.getText()).trim());
            } catch (NumberFormatException e) {
                etPorsi.setError("Isi angka 1 sampai 20");
                return;
            }
            if (porsi < 1 || porsi > 20) {
                etPorsi.setError("Isi angka 1 sampai 20");
                return;
            }

            long batasBaru = 0;
            String teks = String.valueOf(etBatas.getText()).trim();
            if (!teks.isEmpty()) {
                try {
                    batasBaru = Long.parseLong(teks);
                } catch (NumberFormatException e) {
                    etBatas.setError("Nominal tidak valid");
                    return;
                }
            }

            prefs.simpanUsername(String.valueOf(etUsername.getText()).trim());
            prefs.simpanPorsiBawaan(porsi);
            prefs.simpanBatasBiaya(batasBaru);

            Anim.putar(ivHeader);
            Toast.makeText(requireContext(), "Pengaturan tersimpan", Toast.LENGTH_SHORT).show();
        });
    }
}
