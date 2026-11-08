package com.umko.resepanakkost;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PrefsManager prefs = new PrefsManager(this);

        // Tema tersimpan diterapkan sebelum layar pertama tampil,
        // jadi setelah force stop tema gelap langsung terpasang.
        AppCompatDelegate.setDefaultNightMode(prefs.isTemaGelap()
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        // Isi resep bawaan sekali saja pada pemakaian pertama.
        AppDatabase.IO.execute(() -> {
            ResepDao dao = AppDatabase.get(this).resepDao();
            if (!prefs.sudahSeed() && dao.jumlah() == 0) {
                SeedData.isi(dao);
            }
            prefs.tandaiSeed();
        });
    }
}
