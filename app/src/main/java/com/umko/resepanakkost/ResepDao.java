package com.umko.resepanakkost;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResepDao {

    @Insert
    long tambahResep(Resep resep);

    @Insert
    void tambahBahan(List<Bahan> bahan);

    @Delete
    void hapus(Resep resep);

    @Query("SELECT COUNT(*) FROM resep")
    int jumlah();

    @Query("SELECT * FROM resep ORDER BY id DESC")
    List<Resep> semua();

    /** Dipakai saat batas biaya di SharedPreferences diisi. */
    @Query("SELECT * FROM resep WHERE biaya <= :batas ORDER BY id DESC")
    List<Resep> dalamBatas(long batas);

    @Query("SELECT * FROM resep WHERE id = :id")
    Resep cari(int id);

    @Query("SELECT * FROM bahan WHERE resepId = :id ORDER BY id")
    List<Bahan> bahanDari(int id);

    /** Daftar bahan untuk Cek Kulkas, tanpa bahan dasar yang dianggap selalu ada. */
    @Query("SELECT DISTINCT LOWER(nama) AS n FROM bahan "
            + "WHERE LOWER(nama) NOT IN ('garam', 'minyak goreng', 'air') ORDER BY n")
    List<String> namaBahanKulkas();

    /** Berapa bahan tiap resep yang cocok dengan bahan yang dicentang. */
    @Query("SELECT resepId, COUNT(DISTINCT LOWER(nama)) AS cocok FROM bahan "
            + "WHERE LOWER(nama) IN (:terpilih) GROUP BY resepId")
    List<Kecocokan> cocokDengan(List<String> terpilih);

    /** Total bahan tiap resep, tanpa bahan dasar. */
    @Query("SELECT resepId, COUNT(DISTINCT LOWER(nama)) AS cocok FROM bahan "
            + "WHERE LOWER(nama) NOT IN ('garam', 'minyak goreng', 'air') GROUP BY resepId")
    List<Kecocokan> totalBahan();
}
