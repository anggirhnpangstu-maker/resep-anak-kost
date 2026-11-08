package com.umko.resepanakkost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.VH> {

    public interface OnKlik {
        void klik(ResepItem item);
    }

    private final List<ResepItem> data = new ArrayList<>();
    private final OnKlik listener;
    private int terakhirDianimasi = -1;

    public ResepAdapter(OnKlik listener) {
        this.listener = listener;
    }

    public void setData(List<ResepItem> baru) {
        data.clear();
        data.addAll(baru);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ResepItem item = data.get(position);
        h.tvNama.setText(item.resep.nama);
        h.tvInfo.setText(item.info);
        if (item.ket == null || item.ket.isEmpty()) {
            h.tvKet.setVisibility(View.GONE);
        } else {
            h.tvKet.setVisibility(View.VISIBLE);
            h.tvKet.setText(item.ket);
        }

        View v = h.itemView;
        v.animate().cancel();
        if (position > terakhirDianimasi) {
            terakhirDianimasi = position;
            v.setAlpha(0f);
            v.setTranslationY(Anim.dp(v, 28));
            v.animate().alpha(1f).translationY(0f)
                    .setStartDelay(Math.min(position, 8) * 45L).setDuration(320).start();
        } else {
            v.setAlpha(1f);
            v.setTranslationY(0f);
        }

        v.setOnClickListener(x -> {
            Anim.pop(h.ivIkon);
            listener.klik(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final android.widget.ImageView ivIkon;
        final TextView tvNama, tvInfo, tvKet;

        VH(@NonNull View itemView) {
            super(itemView);
            ivIkon = itemView.findViewById(R.id.ivIkon);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            tvKet = itemView.findViewById(R.id.tvKet);
        }
    }
}
