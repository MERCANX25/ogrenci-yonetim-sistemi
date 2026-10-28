package ogrenci;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class OgrenciUygulamasiTest {

    private final Sinif sinif = new Sinif();
    private String cikti;

    private void calistir(String... satirlar) {
        var tampon = new ByteArrayOutputStream();
        var cikis = new PrintStream(tampon, true, StandardCharsets.UTF_8);
        new OgrenciUygulamasi(sinif, new Scanner(String.join("\n", satirlar) + "\n"), cikis).calistir();
        cikti = tampon.toString(StandardCharsets.UTF_8);
    }

    @Test
    void ogrenciEklenipListelenir() {
        calistir("1", "Ayşe Yılmaz", "60", "80", "2", "0");
        assertEquals(1, sinif.istatistik().ogrenciSayisi());
        assertTrue(cikti.contains("Ayşe Yılmaz eklendi. Ortalama: 72,00 (Geçti)"));
        assertTrue(cikti.contains("Ayşe Yılmaz"));
    }

    @Test
    void harfVeAralikDisiNotGirilinceTekrarSorulur() {
        calistir("1", "Ali Kaya", "elli", "150", "45", "55", "0");
        assertTrue(cikti.contains("Lütfen bir sayı girin."));
        assertTrue(cikti.contains("Not 0 ile 100 arasında olmalı."));
        assertEquals(45, sinif.bul("Ali Kaya").orElseThrow().vize());
    }

    @Test
    void menudeHarfGirilinceCokmez() {
        calistir("abc", "9", "0");
        assertTrue(cikti.contains("Lütfen bir sayı girin."));
        assertTrue(cikti.contains("Geçersiz seçim"));
    }

    @Test
    void ayniOgrenciIkinciKezEklenmez() {
        calistir("1", "Ali Kaya", "50", "50", "1", "ali kaya", "0");
        assertTrue(cikti.contains("zaten kayıtlı"));
        assertEquals(1, sinif.istatistik().ogrenciSayisi());
    }

    @Test
    void notlarGuncellenirVeOgrenciSilinir() {
        calistir("1", "Deniz Ak", "20", "20", "5", "deniz ak", "70", "70", "6", "DENİZ AK", "0");
        assertTrue(cikti.contains("Deniz Ak güncellendi. Ortalama: 70,00 (Geçti)"));
        assertTrue(cikti.contains("Deniz Ak silindi."));
        assertTrue(sinif.bosMu());
    }

    @Test
    void olmayanOgrenciArananincaBulunamadiYazar() {
        calistir("4", "Hiç Kimse", "0");
        assertTrue(cikti.contains("Hiç Kimse adlı öğrenci bulunamadı."));
    }

    @Test
    void istatistikVeSiralamaYazdirilir() {
        calistir("1", "Cem", "40", "40", "1", "Zeynep", "90", "90", "3", "7", "0");
        String tablo = cikti.substring(cikti.indexOf("Ad Soyad"));
        assertTrue(tablo.indexOf("Zeynep") < tablo.indexOf("Cem"));
        assertTrue(cikti.contains("Öğrenci sayısı    : 2 (1 geçti, 1 kaldı)"));
        assertTrue(cikti.contains("Sınıf ortalaması  : 65,00"));
    }

    @Test
    void girdiBitinceProgramKapanir() {
        calistir("1", "Yarım");
        assertTrue(sinif.bosMu());
    }
}
