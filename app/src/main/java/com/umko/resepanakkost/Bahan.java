package com.umko.resepanakkost;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "bahan",
        foreignKeys = @ForeignKey(
                entity = Resep.class,
                parentColumns = "id",
                childColumns = "resepId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("resepId")})
public class Bahan {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int resepId;
    public String nama;
    /** Jumlah untuk 1 porsi. */
    public double jumlah;
    public String satuan;

    public Bahan(int resepId, String nama, double jumlah, String satuan) {
        this.resepId = resepId;
        this.nama = nama;
        this.jumlah = jumlah;
        this.satuan = satuan;
    }
}
