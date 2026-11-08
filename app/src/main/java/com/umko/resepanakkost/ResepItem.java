package com.umko.resepanakkost;

/** Pembungkus resep untuk ditampilkan di daftar, lengkap dengan teks tambahan. */
public class ResepItem {
    public final Resep resep;
    public final String info;
    public final String ket;
    public float rasio;

    public ResepItem(Resep resep, String info, String ket) {
        this.resep = resep;
        this.info = info;
        this.ket = ket;
    }
}
