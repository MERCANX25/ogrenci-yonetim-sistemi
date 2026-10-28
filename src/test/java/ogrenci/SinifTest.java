package ogrenci;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SinifTest {

    @ParameterizedTest
    @CsvSource({
            "50, 50, 50.0, true",
            "100, 0, 40.0, false",
            "0, 84, 50.4, true",
            "40, 56, 49.6, false",
    })
    void ortalamaVizeninYuzdeKirkiFinalinYuzdeAltmisidir(int vize, int fin, double ortalama, boolean gecti) {
        Ogrenci o = new Ogrenci("Test Öğrenci", vize, fin);
        assertEquals(ortalama, o.ortalama(), 1e-9);
        assertEquals(gecti, o.gectiMi());
    }

    @Test
    void gecersizNotVeBosAdReddedilir() {
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci("Ali", -1, 50));
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci("Ali", 50, 101));
        assertThrows(IllegalArgumentException.class, () -> new Ogrenci("  ", 50, 50));
    }

    @Test
    void adAramasiTurkceBuyukKucukHarfVeBosluklardanEtkilenmez() {
        Sinif sinif = new Sinif();
        sinif.ekle("İrem  Işık", 70, 80);
        assertTrue(sinif.bul("irem ışık").isPresent());
        assertTrue(sinif.bul("  İREM IŞIK ").isPresent());
        assertFalse(sinif.bul("irem isik").isPresent());
        assertEquals("İrem Işık", sinif.bul("irem ışık").orElseThrow().adSoyad());
    }

    @Test
    void ayniOgrenciIkiKezEklenemez() {
        Sinif sinif = new Sinif();
        sinif.ekle("Ayşe Yılmaz", 60, 70);
        assertThrows(IllegalArgumentException.class, () -> sinif.ekle("AYŞE YILMAZ", 10, 10));
    }

    @Test
    void silinenOgrenciListedenCikar() {
        Sinif sinif = new Sinif();
        sinif.ekle("Ali Kaya", 50, 50);
        sinif.ekle("Veli Can", 60, 60);
        assertEquals("Ali Kaya", sinif.sil("ali kaya").orElseThrow().adSoyad());
        assertTrue(sinif.bul("Ali Kaya").isEmpty());
        assertTrue(sinif.sil("Olmayan Kişi").isEmpty());
    }

    @Test
    void basariSiralamasiOrtalamayaGoreAzalanVeEsitlikteAdaGore() {
        Sinif sinif = new Sinif();
        sinif.ekle("Cem", 40, 40);
        sinif.ekle("Zeynep", 90, 90);
        sinif.ekle("Burak", 70, 70);
        sinif.ekle("Ahmet", 70, 70);
        List<String> sira = new ArrayList<>();
        sinif.basariSirasiyla().forEach(o -> sira.add(o.adSoyad()));
        assertEquals(List.of("Zeynep", "Ahmet", "Burak", "Cem"), sira);
    }

    @Test
    void istatistikHesaplanir() {
        Sinif sinif = new Sinif();
        sinif.ekle("A", 100, 100);
        sinif.ekle("B", 30, 30);
        sinif.ekle("C", 50, 50);
        Sinif.Istatistik i = sinif.istatistik();
        assertEquals(3, i.ogrenciSayisi());
        assertEquals(2, i.gecenSayisi());
        assertEquals(1, i.kalanSayisi());
        assertEquals(60, i.sinifOrtalamasi(), 1e-9);
        assertEquals(100, i.enYuksek(), 1e-9);
        assertEquals(30, i.enDusuk(), 1e-9);
    }

    @Test
    void bosSinifinIstatistigiSifirdir() {
        Sinif.Istatistik i = new Sinif().istatistik();
        assertEquals(0, i.ogrenciSayisi());
        assertEquals(0, i.sinifOrtalamasi());
    }

    @Test
    void notlarGuncellenebilir() {
        Sinif sinif = new Sinif();
        Ogrenci o = sinif.ekle("Deniz", 20, 20);
        assertFalse(o.gectiMi());
        o.notlariGuncelle(60, 60);
        assertTrue(sinif.bul("deniz").orElseThrow().gectiMi());
    }
}
