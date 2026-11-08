package com.umko.resepanakkost;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment f;
            if (id == R.id.menu_beranda) {
                f = new BerandaFragment();
            } else if (id == R.id.menu_kulkas) {
                f = new KulkasFragment();
            } else {
                f = new PengaturanFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container, f)
                    .commit();

            // ikon menu yang dipilih ikut membesar memantul
            View ikon = nav.findViewById(id);
            if (ikon != null) Anim.pop(ikon);
            return true;
        });

        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.menu_beranda);
        }
    }
}
