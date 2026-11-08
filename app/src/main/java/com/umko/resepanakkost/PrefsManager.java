package com.umko.resepanakkost;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Semua akses SharedPreferences dibungkus di kelas ini.
 * Berkas: shared_prefs/resep_prefs.xml (MODE_PRIVATE).
 *
 * Yang disimpan hanya data kecil berbentuk key-value: username, tema gelap,
 * jumlah porsi bawaan, batas biaya per porsi, dan penanda resep bawaan sudah terisi.
 * Resep dan bahan disimpan di Room karena banyak dan perlu query.
 * Alamat kos, nomor HP, dan posisi langkah memasak tidak disimpan.
 */
public class PrefsManager {

    private static final String PREFS_NAME = "resep_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TEMA = "tema_gelap";
    private static final String KEY_PORSI = "porsi_bawaan";
    private static final String KEY_BATAS = "batas_biaya";
    private static final String KEY_SEED = "seed_selesai";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void simpanUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String ambilUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public void simpanTema(boolean gelap) {
        prefs.edit().putBoolean(KEY_TEMA, gelap).apply();
    }

    public boolean isTemaGelap() {
        return prefs.getBoolean(KEY_TEMA, false);
    }

    public void simpanPorsiBawaan(int porsi) {
        prefs.edit().putInt(KEY_PORSI, porsi).apply();
    }

    public int ambilPorsiBawaan() {
        return prefs.getInt(KEY_PORSI, 1);
    }

    /** 0 berarti tanpa batas. */
    public void simpanBatasBiaya(long batas) {
        prefs.edit().putLong(KEY_BATAS, batas).apply();
    }

    public long ambilBatasBiaya() {
        return prefs.getLong(KEY_BATAS, 0L);
    }

    public boolean sudahSeed() {
        return prefs.getBoolean(KEY_SEED, false);
    }

    public void tandaiSeed() {
        prefs.edit().putBoolean(KEY_SEED, true).apply();
    }
}
