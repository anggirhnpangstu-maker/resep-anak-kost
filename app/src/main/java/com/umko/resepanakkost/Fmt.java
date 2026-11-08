package com.umko.resepanakkost;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class Fmt {

    /** Bahan yang dianggap selalu ada di dapur kos. */
    public static final List<String> DASAR = Arrays.asList("garam", "minyak goreng", "air");

    private static final DecimalFormat DF = new DecimalFormat("#.##",
            DecimalFormatSymbols.getInstance(Locale.forLanguageTag("id-ID")));

    private Fmt() {
    }

    public static String jumlah(double nilai) {
        return DF.format(nilai);
    }

    public static String kapital(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
