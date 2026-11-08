package com.umko.resepanakkost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cek Kulkas: pengguna mencentang bahan yang ada, aplikasi mencocokkannya dengan
 * bahan tiap resep di Room. Bahan yang dicentang hanya berlaku sementara,
 * sengaja tidak disimpan.
 */
public class KulkasFragment extends Fragment {

    private ResepDao dao;
    private ChipGroup chipGroup;
    private ResepAdapter adapter;
    private RecyclerView rv;
    private TextView tvJudul, tvKosong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kulkas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dao = AppDatabase.get(requireContext()).resepDao();
        chipGroup = view.findViewById(R.id.chipBahan);
        rv = view.findViewById(R.id.rvHasil);
        tvJudul = view.findViewById(R.id.tvHasilJudul);
        tvKosong = view.findViewById(R.id.tvKosong);
        ImageView ivHeader = view.findViewById(R.id.ivHeader);
        View btnCari = view.findViewById(R.id.btnCari);
        View btnKosongkan = view.findViewById(R.id.btnKosongkan);

        adapter = new ResepAdapter(item -> {
            Intent i = new Intent(requireContext(), DetailResepActivity.class);
            i.putExtra("resep_id", item.resep.id);
            startActivity(i);
        });
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // Menu Cek Kulkas: ikon kulkas bergoyang seperti pintunya baru dibuka
        Anim.goyang(ivHeader);
        ivHeader.setOnClickListener(Anim::goyang);

        Anim.efekTekan(btnCari);
        Anim.efekTekan(btnKosongkan);
        btnCari.setOnClickListener(v -> cari());
        btnKosongkan.setOnClickListener(v -> {
            chipGroup.clearCheck();
            adapter.setData(new ArrayList<>());
            tvJudul.setVisibility(View.GONE);
            tvKosong.setVisibility(View.GONE);
        });

        muatBahan();
    }

    private void muatBahan() {
        AppDatabase.IO.execute(() -> {
            List<String> nama = dao.namaBahanKulkas();
            ui(() -> {
                if (!isAdded()) return;
                chipGroup.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                for (int i = 0; i < nama.size(); i++) {
                    Chip chip = (Chip) inflater.inflate(R.layout.item_chip, chipGroup, false);
                    chip.setText(Fmt.kapital(nama.get(i)));
                    chip.setTag(nama.get(i));
                    chipGroup.addView(chip);
                    Anim.muncul(chip, i * 40L);
                }
            });
        });
    }

    private void cari() {
        List<String> terpilih = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip c = (Chip) chipGroup.getChildAt(i);
            if (c.isChecked()) terpilih.add((String) c.getTag());
        }
        if (terpilih.isEmpty()) {
            Toast.makeText(requireContext(), "Centang minimal satu bahan",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.IO.execute(() -> {
            List<Kecocokan> cocok = dao.cocokDengan(terpilih);
            Map<Integer, Integer> total = new HashMap<>();
            for (Kecocokan k : dao.totalBahan()) total.put(k.resepId, k.cocok);

            List<ResepItem> hasil = new ArrayList<>();
            for (Kecocokan k : cocok) {
                Resep r = dao.cari(k.resepId);
                if (r == null) continue;
                int jumlahBahan = total.containsKey(k.resepId) ? total.get(k.resepId) : k.cocok;

                List<String> kurang = new ArrayList<>();
                for (Bahan b : dao.bahanDari(k.resepId)) {
                    String nm = b.nama.toLowerCase().trim();
                    if (!Fmt.DASAR.contains(nm) && !terpilih.contains(nm)) kurang.add(nm);
                }

                String ket = "Cocok " + k.cocok + " dari " + jumlahBahan + " bahan. "
                        + (kurang.isEmpty()
                        ? "Semua bahan sudah ada."
                        : "Kurang: " + String.join(", ", kurang) + ".");
                ResepItem item = new ResepItem(r,
                        Rupiah.format(r.biaya) + " per porsi - " + r.menit + " menit", ket);
                item.rasio = k.cocok / (float) Math.max(jumlahBahan, 1);
                hasil.add(item);
            }

            Collections.sort(hasil, (a, b) -> {
                int c = Float.compare(b.rasio, a.rasio);
                return c != 0 ? c : Long.compare(a.resep.biaya, b.resep.biaya);
            });

            ui(() -> {
                if (!isAdded()) return;
                adapter.setData(hasil);
                tvJudul.setVisibility(View.VISIBLE);
                tvJudul.setText("Resep yang cocok (" + hasil.size() + ")");
                tvKosong.setVisibility(hasil.isEmpty() ? View.VISIBLE : View.GONE);
                rv.setVisibility(hasil.isEmpty() ? View.GONE : View.VISIBLE);
            });
        });
    }

    private void ui(Runnable r) {
        Activity a = getActivity();
        if (a != null) a.runOnUiThread(r);
    }
}
