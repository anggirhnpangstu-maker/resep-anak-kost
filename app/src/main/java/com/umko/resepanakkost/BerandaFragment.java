package com.umko.resepanakkost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BerandaFragment extends Fragment {

    private PrefsManager prefs;
    private ResepDao dao;
    private ResepAdapter adapter;

    private TextView tvSapaan, tvInfo, tvKosong;
    private ImageView ivAyam;
    private RecyclerView rv;

    private List<Resep> daftarTampil = new ArrayList<>();
    private int urutanReaksi = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beranda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = new PrefsManager(requireContext());
        dao = AppDatabase.get(requireContext()).resepDao();

        tvSapaan = view.findViewById(R.id.tvSapaan);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvKosong = view.findViewById(R.id.tvKosong);
        ivAyam = view.findViewById(R.id.ivAyam);
        rv = view.findViewById(R.id.rvResep);
        View btnAcak = view.findViewById(R.id.btnAcak);
        View fab = view.findViewById(R.id.fabTambah);

        adapter = new ResepAdapter(item -> bukaDetail(item.resep.id));
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        Anim.efekTekan(btnAcak);
        btnAcak.setOnClickListener(v -> acakResep());

        fab.setOnClickListener(v -> {
            Anim.gepeng(v);
            startActivity(new Intent(requireContext(), TambahResepActivity.class));
        });

        // Ayam koki bereaksi berbeda setiap disentuh
        ivAyam.setOnClickListener(v -> Anim.reaksi(v, urutanReaksi++));

        // Animasi saat menu Beranda dibuka
        Anim.pop(ivAyam);
        Anim.pop(fab);
        Anim.muncul(tvSapaan, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Nilai dibaca ulang dari SharedPreferences setiap layar kembali tampil
        String username = prefs.ambilUsername();
        tvSapaan.setText(username.isEmpty() ? "Halo, selamat datang!" : "Halo, " + username + "!");

        long batas = prefs.ambilBatasBiaya();
        tvInfo.setText(batas > 0
                ? "Menampilkan resep maksimal " + Rupiah.format(batas) + " per porsi"
                : "Menampilkan semua resep");

        muat(batas);
    }

    private void muat(long batas) {
        AppDatabase.IO.execute(() -> {
            // Batas biaya dari SharedPreferences menentukan query Room yang dipakai
            List<Resep> daftar = batas > 0 ? dao.dalamBatas(batas) : dao.semua();
            List<ResepItem> items = new ArrayList<>();
            for (Resep r : daftar) {
                items.add(new ResepItem(r,
                        Rupiah.format(r.biaya) + " per porsi - " + r.menit + " menit", null));
            }
            ui(() -> {
                if (!isAdded()) return;
                daftarTampil = daftar;
                adapter.setData(items);
                tvKosong.setVisibility(daftar.isEmpty() ? View.VISIBLE : View.GONE);
                rv.setVisibility(daftar.isEmpty() ? View.GONE : View.VISIBLE);
            });
        });
    }

    private void acakResep() {
        if (daftarTampil.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Belum ada resep yang masuk batas biaya", Toast.LENGTH_SHORT).show();
            return;
        }
        Resep pilihan = daftarTampil.get(new Random().nextInt(daftarTampil.size()));

        View isi = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_acak, null);
        ImageView panci = isi.findViewById(R.id.ivPanci);
        TextView hasil = isi.findViewById(R.id.tvHasil);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(isi)
                .setNegativeButton("Tutup", null)
                .setPositiveButton("Lihat resep", (d, w) -> bukaDetail(pilihan.id))
                .create();
        dialog.show();

        Button lihat = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        lihat.setVisibility(View.GONE);
        Anim.goyang(panci);
        panci.postDelayed(() -> Anim.goyang(panci), 550);

        panci.postDelayed(() -> {
            if (!dialog.isShowing()) return;
            hasil.setText(pilihan.nama + "\n" + Rupiah.format(pilihan.biaya)
                    + " per porsi - " + pilihan.menit + " menit");
            Anim.pop(hasil);
            Anim.lompat(panci);
            lihat.setVisibility(View.VISIBLE);
        }, 1100);
    }

    private void bukaDetail(int id) {
        Intent i = new Intent(requireContext(), DetailResepActivity.class);
        i.putExtra("resep_id", id);
        startActivity(i);
    }

    private void ui(Runnable r) {
        Activity a = getActivity();
        if (a != null) a.runOnUiThread(r);
    }
}
