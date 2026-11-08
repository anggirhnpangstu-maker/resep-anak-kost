package com.umko.resepanakkost;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Resep.class, Bahan.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /** Thread latar bersama untuk semua operasi database. */
    public static final ExecutorService IO = Executors.newSingleThreadExecutor();

    private static volatile AppDatabase instance;

    public abstract ResepDao resepDao();

    public static AppDatabase get(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "resep_db").build();
                }
            }
        }
        return instance;
    }
}
