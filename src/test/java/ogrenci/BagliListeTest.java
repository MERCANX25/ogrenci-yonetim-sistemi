package ogrenci;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

class BagliListeTest {

    private static <T> List<T> listele(BagliListe<T> liste) {
        List<T> sonuc = new ArrayList<>();
        liste.forEach(sonuc::add);
        return sonuc;
    }

    private static BagliListe<Integer> listeOf(Integer... sayilar) {
        BagliListe<Integer> liste = new BagliListe<>();
        for (Integer s : sayilar) {
            liste.ekle(s);
        }
        return liste;
    }

    @Test
    void elemanlarEklenmeSirasiylaDolasilir() {
        BagliListe<Integer> liste = listeOf(3, 1, 2);
        assertEquals(List.of(3, 1, 2), listele(liste));
        assertEquals(3, liste.boyut());
    }

    @Test
    void bastakiElemanSilinebilir() {
        BagliListe<Integer> liste = listeOf(1, 2, 3);
        assertEquals(1, liste.sil(s -> s == 1).orElseThrow());
        assertEquals(List.of(2, 3), listele(liste));
    }

    @Test
    void ortadakiElemanSilinebilir() {
        BagliListe<Integer> liste = listeOf(1, 2, 3);
        liste.sil(s -> s == 2);
        assertEquals(List.of(1, 3), listele(liste));
    }

    @Test
    void sondakiElemanSilininceSonaEklemeDogruCalisir() {
        BagliListe<Integer> liste = listeOf(1, 2, 3);
        liste.sil(s -> s == 3);
        liste.ekle(4);
        assertEquals(List.of(1, 2, 4), listele(liste));
    }

    @Test
    void tekElemanSilinincelisteBosalirVeTekrarKullanilabilir() {
        BagliListe<Integer> liste = listeOf(7);
        liste.sil(s -> s == 7);
        assertTrue(liste.bosMu());
        liste.ekle(8);
        assertEquals(List.of(8), listele(liste));
    }

    @Test
    void olmayanElemanSilinmezVeBoyutDegismez() {
        BagliListe<Integer> liste = listeOf(1, 2);
        assertTrue(liste.sil(s -> s == 9).isEmpty());
        assertEquals(2, liste.boyut());
    }

    @Test
    void bulKosulaUyanIlkElemaniDondurur() {
        BagliListe<Integer> liste = listeOf(1, 4, 6);
        assertEquals(4, liste.bul(s -> s % 2 == 0).orElseThrow());
        assertTrue(liste.bul(s -> s > 10).isEmpty());
    }

    @Test
    void siraliYeniListeDondururAsilListeDegismez() {
        BagliListe<Integer> liste = listeOf(5, 2, 8, 1, 9);
        BagliListe<Integer> sirali = liste.sirali(Comparator.naturalOrder());
        assertEquals(List.of(1, 2, 5, 8, 9), listele(sirali));
        assertEquals(List.of(5, 2, 8, 1, 9), listele(liste));
        sirali.ekle(10);
        assertEquals(List.of(1, 2, 5, 8, 9, 10), listele(sirali));
    }

    @Test
    void siralamaEsitElemanlarinSirasiniKorur() {
        BagliListe<String> liste = new BagliListe<>();
        for (String s : List.of("bb", "a", "cc", "d")) {
            liste.ekle(s);
        }
        assertEquals(List.of("a", "d", "bb", "cc"), listele(liste.sirali(Comparator.comparingInt(String::length))));
    }

    @Test
    void bosListeSiralanabilir() {
        assertTrue(new BagliListe<Integer>().sirali(Comparator.naturalOrder()).bosMu());
    }
}
