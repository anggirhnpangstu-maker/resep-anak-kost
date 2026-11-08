package com.umko.resepanakkost;

import java.text.NumberFormat;
import java.util.Locale;

public final class Rupiah {

    private Rupiah() {
    }

    public static String format(long nilai) {
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
        nf.setMaximumFractionDigits(0);
        return (nilai < 0 ? "-" : "") + "Rp" + nf.format(Math.abs(nilai));
    }
}
