package com.umko.resepanakkost;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "resep")
public class Resep {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nama;
    /** Biaya per porsi dalam rupiah. */
    public long biaya;
    public int menit;
    /** Satu langkah per baris, dipisah karakter newline. */
    public String langkah;
    /** true untuk resep bawaan aplikasi, false untuk resep tambahan pengguna. */
    public boolean bawaan;

    public Resep(String nama, long biaya, int menit, String langkah, boolean bawaan) {
        this.nama = nama;
        this.biaya = biaya;
        this.menit = menit;
        this.langkah = langkah;
        this.bawaan = bawaan;
    }
}
