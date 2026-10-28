package ogrenci;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

/** Konsol menüsü. Giriş ve çıkış dışarıdan verildiği için testte hazır girdiyle çalıştırılabilir. */
public final class OgrenciUygulamasi {

    private static final Locale TURKCE = Locale.of("tr", "TR");
    private static final String CIZGI = "-".repeat(62);

    private final Sinif sinif;
    private final Scanner giris;
    private final PrintStream cikis;

    public OgrenciUygulamasi(Sinif sinif, Scanner giris, PrintStream cikis) {
        this.sinif = sinif;
        this.giris = giris;
        this.cikis = cikis;
    }

    public void calistir() {
        while (true) {
            cikis.println();
            cikis.println("===== ÖĞRENCİ YÖNETİM MENÜSÜ =====");
            cikis.println("1. Yeni öğrenci ekle");
            cikis.println("2. Öğrencileri listele (eklenme sırası)");
            cikis.println("3. Başarı sıralaması");
            cikis.println("4. Öğrenci ara");
            cikis.println("5. Notları güncelle");
            cikis.println("6. Öğrenci sil");
            cikis.println("7. Sınıf istatistikleri");
            cikis.println("0. Çıkış");
            Integer secim = sayiOku("Seçiminiz: ");
            if (secim == null || secim == 0) {
                cikis.println("Programdan çıkılıyor...");
                return;
            }
            switch (secim) {
                case 1 -> ekle();
                case 2 -> tabloYazdir(sinif.eklenmeSirasiyla());
                case 3 -> tabloYazdir(sinif.basariSirasiyla());
                case 4 -> ara();
                case 5 -> guncelle();
                case 6 -> sil();
                case 7 -> istatistikYazdir();
                default -> cikis.println("Geçersiz seçim! 0-7 arası bir sayı girin.");
            }
        }
    }

    private void ekle() {
        String ad = satirOku("Ad-Soyad: ");
        if (ad == null) {
            return;
        }
        if (sinif.bul(ad).isPresent()) {
            cikis.println(ad.trim() + " zaten kayıtlı. Notlarını değiştirmek için 5'i seçin.");
            return;
        }
        Integer vize = notOku("Vize notu: ");
        Integer fin = vize == null ? null : notOku("Final notu: ");
        if (fin == null) {
            return;
        }
        Ogrenci o = sinif.ekle(ad, vize, fin);
        cikis.printf(TURKCE, "%s eklendi. Ortalama: %.2f (%s)%n", o.adSoyad(), o.ortalama(), durum(o));
    }

    private void ara() {
        String ad = satirOku("Aranacak ad-soyad: ");
        if (ad == null) {
            return;
        }
        sinif.bul(ad).ifPresentOrElse(
                o -> {
                    BagliListe<Ogrenci> tek = new BagliListe<>();
                    tek.ekle(o);
                    tabloYazdir(tek);
                },
                () -> cikis.println(ad.trim() + " adlı öğrenci bulunamadı."));
    }

    private void guncelle() {
        String ad = satirOku("Notu güncellenecek öğrencinin ad-soyadı: ");
        if (ad == null) {
            return;
        }
        var bulunan = sinif.bul(ad);
        if (bulunan.isEmpty()) {
            cikis.println(ad.trim() + " adlı öğrenci bulunamadı.");
            return;
        }
        Integer vize = notOku("Yeni vize notu: ");
        Integer fin = vize == null ? null : notOku("Yeni final notu: ");
        if (fin == null) {
            return;
        }
        Ogrenci o = bulunan.get();
        o.notlariGuncelle(vize, fin);
        cikis.printf(TURKCE, "%s güncellendi. Ortalama: %.2f (%s)%n", o.adSoyad(), o.ortalama(), durum(o));
    }

    private void sil() {
        if (sinif.bosMu()) {
            cikis.println("Henüz öğrenci yok.");
            return;
        }
        String ad = satirOku("Silinecek öğrencinin ad-soyadı: ");
        if (ad == null) {
            return;
        }
        sinif.sil(ad).ifPresentOrElse(
                o -> cikis.println(o.adSoyad() + " silindi."),
                () -> cikis.println(ad.trim() + " adlı öğrenci bulunamadı."));
    }

    private void tabloYazdir(BagliListe<Ogrenci> liste) {
        if (liste.bosMu()) {
            cikis.println("Henüz öğrenci yok.");
            return;
        }
        cikis.println(CIZGI);
        cikis.printf("%-24s %6s %6s %10s   %s%n", "Ad Soyad", "Vize", "Final", "Ortalama", "Durum");
        cikis.println(CIZGI);
        for (Ogrenci o : liste) {
            cikis.printf(TURKCE, "%-24s %6d %6d %10.2f   %s%n",
                    o.adSoyad(), o.vize(), o.finalNotu(), o.ortalama(), durum(o));
        }
        cikis.println(CIZGI);
    }

    private void istatistikYazdir() {
        Sinif.Istatistik i = sinif.istatistik();
        if (i.ogrenciSayisi() == 0) {
            cikis.println("Henüz öğrenci yok.");
            return;
        }
        cikis.printf(TURKCE, "%-18s: %d (%d geçti, %d kaldı)%n", "Öğrenci sayısı",
                i.ogrenciSayisi(), i.gecenSayisi(), i.kalanSayisi());
        cikis.printf(TURKCE, "%-18s: %.2f%n", "Sınıf ortalaması", i.sinifOrtalamasi());
        cikis.printf(TURKCE, "%-18s: %.2f%n", "En yüksek", i.enYuksek());
        cikis.printf(TURKCE, "%-18s: %.2f%n", "En düşük", i.enDusuk());
    }

    private static String durum(Ogrenci o) {
        return o.gectiMi() ? "Geçti" : "Kaldı";
    }

    /** Boş olmayan bir satır okur. Girdi biterse null döner. */
    private String satirOku(String soru) {
        while (true) {
            cikis.print(soru);
            if (!giris.hasNextLine()) {
                cikis.println();
                return null;
            }
            String satir = giris.nextLine();
            if (!satir.isBlank()) {
                return satir;
            }
            cikis.println("Boş bırakılamaz.");
        }
    }

    private Integer sayiOku(String soru) {
        while (true) {
            String satir = satirOku(soru);
            if (satir == null) {
                return null;
            }
            try {
                return Integer.parseInt(satir.trim());
            } catch (NumberFormatException e) {
                cikis.println("Lütfen bir sayı girin.");
            }
        }
    }

    private Integer notOku(String soru) {
        while (true) {
            Integer not = sayiOku(soru);
            if (not == null || (not >= 0 && not <= 100)) {
                return not;
            }
            cikis.println("Not 0 ile 100 arasında olmalı.");
        }
    }
}
