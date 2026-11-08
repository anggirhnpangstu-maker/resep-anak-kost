package com.umko.resepanakkost;

import java.util.ArrayList;
import java.util.List;

/** Resep hemat bawaan. Takaran bahan untuk 1 porsi. */
public final class SeedData {

    private SeedData() {
    }

    public static void isi(ResepDao dao) {
        r(dao, "Nasi Goreng Telur", 7000, 10,
                "Iris halus bawang putih.\n"
                        + "Panaskan minyak, tumis bawang putih sampai harum.\n"
                        + "Masukkan telur, aduk hingga setengah matang.\n"
                        + "Tambahkan nasi, kecap manis, dan garam, lalu aduk rata.\n"
                        + "Masak hingga nasi kering dan wangi, lalu angkat.",
                "nasi|1|piring", "telur|1|butir", "bawang putih|2|siung",
                "kecap manis|1|sdm", "minyak goreng|2|sdm", "garam|0.25|sdt");

        r(dao, "Telur Dadar Bawang", 5000, 8,
                "Iris tipis bawang merah.\n"
                        + "Kocok telur bersama bawang merah dan garam.\n"
                        + "Panaskan minyak di wajan.\n"
                        + "Tuang adonan, masak hingga kecokelatan, balik sekali.\n"
                        + "Angkat dan sajikan selagi hangat.",
                "telur|2|butir", "bawang merah|2|siung", "garam|0.25|sdt", "minyak goreng|1|sdm");

        r(dao, "Mi Instan Telur Sayur", 7000, 8,
                "Didihkan air dalam panci.\n"
                        + "Masukkan mi dan bayam, masak sekitar tiga menit.\n"
                        + "Pecahkan telur ke dalam rebusan.\n"
                        + "Tambahkan bumbu mi, aduk perlahan.\n"
                        + "Angkat saat telur sudah matang.",
                "mi instan|1|bungkus", "telur|1|butir", "bayam|0.5|ikat", "air|400|ml");

        r(dao, "Tumis Kangkung", 4000, 10,
                "Petik kangkung dan cuci bersih.\n"
                        + "Iris bawang putih dan cabai.\n"
                        + "Tumis bawang putih dan cabai sampai harum.\n"
                        + "Masukkan kangkung dan garam, aduk cepat.\n"
                        + "Angkat saat kangkung layu tetapi masih hijau.",
                "kangkung|1|ikat", "bawang putih|2|siung", "cabai|2|buah",
                "minyak goreng|1|sdm", "garam|0.25|sdt");

        r(dao, "Tempe Orek", 4000, 15,
                "Potong tempe kecil-kecil, goreng sebentar.\n"
                        + "Iris bawang merah, bawang putih, dan cabai.\n"
                        + "Tumis irisan bumbu sampai harum.\n"
                        + "Masukkan tempe dan kecap manis, aduk rata.\n"
                        + "Masak hingga bumbu meresap dan agak kering.",
                "tempe|0.5|papan", "kecap manis|2|sdm", "bawang merah|2|siung",
                "bawang putih|1|siung", "cabai|2|buah", "minyak goreng|1|sdm");

        r(dao, "Tahu Telur", 5000, 10,
                "Hancurkan tahu dengan garpu.\n"
                        + "Campur tahu dengan telur dan garam.\n"
                        + "Panaskan minyak di wajan.\n"
                        + "Tuang adonan dan masak dengan api kecil.\n"
                        + "Balik sekali, angkat saat kedua sisi kecokelatan.",
                "tahu|2|potong", "telur|1|butir", "garam|0.25|sdt", "minyak goreng|1|sdm");

        r(dao, "Sayur Bayam Bening", 4000, 15,
                "Petik daun bayam dan cuci bersih.\n"
                        + "Didihkan air bersama irisan bawang merah dan bawang putih.\n"
                        + "Tambahkan garam.\n"
                        + "Masukkan bayam, masak sebentar sampai layu.\n"
                        + "Angkat dan sajikan hangat.",
                "bayam|1|ikat", "bawang putih|2|siung", "bawang merah|2|siung",
                "garam|0.5|sdt", "air|400|ml");

        r(dao, "Roti Bakar Pisang", 5000, 7,
                "Oles margarin di satu sisi tiap roti.\n"
                        + "Lumatkan pisang dengan garpu.\n"
                        + "Letakkan pisang di atas roti, tutup dengan roti kedua.\n"
                        + "Panggang di wajan dengan api kecil.\n"
                        + "Balik hingga kedua sisi kecokelatan.",
                "roti tawar|2|lembar", "pisang|1|buah", "margarin|1|sdm");

        r(dao, "Pisang Goreng Tepung", 6000, 15,
                "Campur tepung terigu, garam, dan air sampai adonan kental.\n"
                        + "Potong pisang menjadi dua bagian.\n"
                        + "Panaskan minyak cukup banyak.\n"
                        + "Celupkan pisang ke adonan lalu goreng.\n"
                        + "Angkat saat kuning keemasan dan tiriskan.",
                "pisang|3|buah", "tepung terigu|5|sdm", "garam|0.1|sdt",
                "minyak goreng|100|ml", "air|3|sdm");

        r(dao, "Orak-arik Sosis Telur", 6000, 10,
                "Iris sosis dan bawang putih.\n"
                        + "Tumis bawang putih sampai harum.\n"
                        + "Masukkan sosis, masak sebentar.\n"
                        + "Tuang telur, aduk hingga orak-arik.\n"
                        + "Tambahkan kecap manis, aduk rata, lalu angkat.",
                "sosis|2|buah", "telur|1|butir", "bawang putih|1|siung",
                "kecap manis|1|sdm", "minyak goreng|1|sdm");

        r(dao, "Nasi Kecap Telur Ceplok", 5000, 7,
                "Panaskan minyak, goreng bawang merah iris sampai harum.\n"
                        + "Pecahkan telur dan masak sampai matang sesuai selera.\n"
                        + "Letakkan nasi di piring.\n"
                        + "Taruh telur di atas nasi.\n"
                        + "Siram dengan kecap manis.",
                "nasi|1|piring", "telur|1|butir", "kecap manis|2|sdm",
                "bawang merah|1|siung", "minyak goreng|1|sdm");

        r(dao, "Omelet Mi", 8000, 10,
                "Rebus mi sebentar, tiriskan.\n"
                        + "Kocok telur bersama irisan cabai dan sedikit bumbu mi.\n"
                        + "Campur mi dengan kocokan telur.\n"
                        + "Panaskan minyak, tuang adonan.\n"
                        + "Masak dengan api kecil, balik sekali sampai matang.",
                "mi instan|1|bungkus", "telur|2|butir", "cabai|1|buah",
                "minyak goreng|1|sdm", "air|300|ml");
    }

    /** Format bahan: "nama|jumlah|satuan". */
    private static void r(ResepDao dao, String nama, long biaya, int menit,
                          String langkah, String... bahan) {
        long id = dao.tambahResep(new Resep(nama, biaya, menit, langkah, true));
        List<Bahan> daftar = new ArrayList<>();
        for (String b : bahan) {
            String[] p = b.split("\\|");
            daftar.add(new Bahan((int) id, p[0], Double.parseDouble(p[1]), p[2]));
        }
        dao.tambahBahan(daftar);
    }
}
