package ogrenci;

import java.util.Comparator;
import java.util.Optional;

/** Öğrencileri bağlı listede tutar. Aynı ad-soyadla ikinci öğrenci eklenemez. */
public final class Sinif {

    /** Sınıfın not özeti. Öğrenci yoksa ortalama, en yüksek ve en düşük 0'dır. */
    public record Istatistik(int ogrenciSayisi, int gecenSayisi, double sinifOrtalamasi,
                             double enYuksek, double enDusuk) {

        public int kalanSayisi() {
            return ogrenciSayisi - gecenSayisi;
        }
    }

    private final BagliListe<Ogrenci> ogrenciler = new BagliListe<>();

    public Ogrenci ekle(String adSoyad, int vize, int finalNotu) {
        if (bul(adSoyad).isPresent()) {
            throw new IllegalArgumentException(adSoyad.trim() + " zaten kayıtlı.");
        }
        Ogrenci ogrenci = new Ogrenci(adSoyad, vize, finalNotu);
        ogrenciler.ekle(ogrenci);
        return ogrenci;
    }

    public Optional<Ogrenci> bul(String adSoyad) {
        return ogrenciler.bul(o -> o.adiEslesir(adSoyad));
    }

    public Optional<Ogrenci> sil(String adSoyad) {
        return ogrenciler.sil(o -> o.adiEslesir(adSoyad));
    }

    public BagliListe<Ogrenci> eklenmeSirasiyla() {
        return ogrenciler;
    }

    /** Ortalamaya göre büyükten küçüğe; eşitlikte ada göre. */
    public BagliListe<Ogrenci> basariSirasiyla() {
        return ogrenciler.sirali(Comparator.comparingDouble(Ogrenci::ortalama).reversed()
                .thenComparing(o -> Ogrenci.anahtar(o.adSoyad())));
    }

    public Istatistik istatistik() {
        int sayi = 0;
        int gecen = 0;
        double toplam = 0;
        double enYuksek = 0;
        double enDusuk = 0;
        for (Ogrenci o : ogrenciler) {
            double ort = o.ortalama();
            if (sayi == 0) {
                enYuksek = ort;
                enDusuk = ort;
            }
            enYuksek = Math.max(enYuksek, ort);
            enDusuk = Math.min(enDusuk, ort);
            toplam += ort;
            if (o.gectiMi()) {
                gecen++;
            }
            sayi++;
        }
        return new Istatistik(sayi, gecen, sayi == 0 ? 0 : toplam / sayi, enYuksek, enDusuk);
    }

    public boolean bosMu() {
        return ogrenciler.bosMu();
    }
}
