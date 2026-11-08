package com.umko.resepanakkost;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/** Kumpulan animasi kecil yang dipakai di seluruh aplikasi. */
public final class Anim {

    private Anim() {
    }

    public static float dp(View v, float nilai) {
        return nilai * v.getResources().getDisplayMetrics().density;
    }

    /** Membesar dari kecil dengan efek memantul. */
    public static void pop(View v) {
        v.animate().cancel();
        v.setScaleX(0.6f);
        v.setScaleY(0.6f);
        v.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(380)
                .setInterpolator(new OvershootInterpolator(3f)).start();
    }

    /** Bergoyang kiri kanan. */
    public static void goyang(View v) {
        ObjectAnimator a = ObjectAnimator.ofFloat(v, View.ROTATION,
                0f, -12f, 12f, -8f, 8f, -4f, 4f, 0f);
        a.setDuration(600);
        a.start();
    }

    /** Melompat lalu mendarat dengan pantulan. */
    public static void lompat(View v) {
        v.animate().cancel();
        v.animate().translationY(-dp(v, 36)).setStartDelay(0).setDuration(180)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(() -> v.animate().translationY(0).setDuration(420)
                        .setInterpolator(new BounceInterpolator()).start())
                .start();
    }

    /** Berputar satu putaran penuh. */
    public static void putar(View v) {
        v.animate().rotationBy(360f).setStartDelay(0).setDuration(700)
                .setInterpolator(new OvershootInterpolator(1.2f)).start();
    }

    /** Memipih sebentar seperti ditekan, lalu kembali. */
    public static void gepeng(View v) {
        v.animate().cancel();
        v.animate().scaleX(1.12f).scaleY(0.85f).setStartDelay(0).setDuration(110)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(300)
                        .setInterpolator(new OvershootInterpolator(3f)).start())
                .start();
    }

    /** Muncul dari bawah sambil memudar masuk. */
    public static void muncul(View v, long tunda) {
        v.setAlpha(0f);
        v.setTranslationY(dp(v, 24));
        v.animate().alpha(1f).translationY(0f).setStartDelay(tunda).setDuration(350)
                .setInterpolator(new DecelerateInterpolator()).start();
    }

    /** Naik turun pelan tanpa henti. */
    public static void melayang(View v) {
        ObjectAnimator a = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, 0f, -dp(v, 12));
        a.setDuration(1300);
        a.setRepeatMode(ObjectAnimator.REVERSE);
        a.setRepeatCount(ObjectAnimator.INFINITE);
        a.setInterpolator(new AccelerateInterpolator(0.6f));
        a.start();
    }

    /** Reaksi maskot yang bergantian: goyang, lompat, putar. */
    public static void reaksi(View v, int urutan) {
        int pilihan = Math.abs(urutan) % 3;
        if (pilihan == 0) goyang(v);
        else if (pilihan == 1) lompat(v);
        else putar(v);
    }

    /** Efek tekan pada tombol: mengecil saat disentuh. */
    @SuppressLint("ClickableViewAccessibility")
    public static void efekTekan(View v) {
        v.setOnTouchListener((view, e) -> {
            int aksi = e.getActionMasked();
            if (aksi == MotionEvent.ACTION_DOWN) {
                view.animate().scaleX(0.94f).scaleY(0.94f).setDuration(90).start();
            } else if (aksi == MotionEvent.ACTION_UP || aksi == MotionEvent.ACTION_CANCEL) {
                view.animate().scaleX(1f).scaleY(1f).setDuration(160)
                        .setInterpolator(new OvershootInterpolator(3f)).start();
            }
            return false;
        });
    }
}
