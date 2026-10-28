package ogrenci;

import java.util.Locale;

/** Bir öğrenci ve notları. Ortalama: vizenin %40'ı + finalin %60'ı; 50 ve üzeri geçer. */
public final class Ogrenci {

    public static final double VIZE_ORANI = 0.4;
    public static final double FINAL_ORANI = 0.6;
    public static final double GECME_NOTU = 50;

    private static final Locale TURKCE = Locale.of("tr", "TR");

    private final String adSoyad;
    private int vize;
    private int finalNotu;

    public Ogrenci(String adSoyad, int vize, int finalNotu) {
        if (adSoyad == null || adSoyad.isBlank()) {
            throw new IllegalArgumentException("Ad-soyad boş olamaz.");
        }
        this.adSoyad = adSoyad.trim().replaceAll("\\s+", " ");
        notlariGuncelle(vize, finalNotu);
    }

    public void notlariGuncelle(int vize, int finalNotu) {
        notuDogrula("Vize", vize);
        notuDogrula("Final", finalNotu);
        this.vize = vize;
        this.finalNotu = finalNotu;
    }

    private static void notuDogrula(String ad, int not) {
        if (not < 0 || not > 100) {
            throw new IllegalArgumentException(ad + " notu 0 ile 100 arasında olmalı.");
        }
    }

    public String adSoyad() {
        return adSoyad;
    }

    public int vize() {
        return vize;
    }

    public int finalNotu() {
        return finalNotu;
    }

    public double ortalama() {
        return vize * VIZE_ORANI + finalNotu * FINAL_ORANI;
    }

    public boolean gectiMi() {
        return ortalama() >= GECME_NOTU;
    }

    /** "Ayşe Yılmaz", "  ayşe   YILMAZ " ve "AYŞE YILMAZ" aynı kişi sayılır (Türkçe İ/ı kurallarıyla). */
    public boolean adiEslesir(String aranan) {
        return anahtar(adSoyad).equals(anahtar(aranan));
    }

    static String anahtar(String ad) {
        return ad == null ? "" : ad.trim().replaceAll("\\s+", " ").toLowerCase(TURKCE);
    }
}
