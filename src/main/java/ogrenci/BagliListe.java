package ogrenci;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Tek yönlü bağlı liste. Her düğüm bir sonrakini gösterir.
 * Son düğüm de tutulduğu için sona ekleme listeyi baştan dolaşmadan O(1) sürede yapılır.
 */
public final class BagliListe<T> implements Iterable<T> {

    private static final class Dugum<T> {
        T veri;
        Dugum<T> sonraki;

        Dugum(T veri) {
            this.veri = veri;
        }
    }

    private Dugum<T> bas;
    private Dugum<T> son;
    private int boyut;

    /** Sona ekler: O(1). */
    public void ekle(T veri) {
        Dugum<T> yeni = new Dugum<>(veri);
        if (bas == null) {
            bas = yeni;
        } else {
            son.sonraki = yeni;
        }
        son = yeni;
        boyut++;
    }

    /** Koşula uyan ilk elemanı bulur: O(n). */
    public Optional<T> bul(Predicate<? super T> kosul) {
        for (Dugum<T> d = bas; d != null; d = d.sonraki) {
            if (kosul.test(d.veri)) {
                return Optional.of(d.veri);
            }
        }
        return Optional.empty();
    }

    /** Koşula uyan ilk elemanı listeden çıkarır ve döndürür: O(n). */
    public Optional<T> sil(Predicate<? super T> kosul) {
        Dugum<T> onceki = null;
        for (Dugum<T> d = bas; d != null; onceki = d, d = d.sonraki) {
            if (!kosul.test(d.veri)) {
                continue;
            }
            if (onceki == null) {
                bas = d.sonraki;
            } else {
                onceki.sonraki = d.sonraki;
            }
            if (d == son) {
                son = onceki;
            }
            boyut--;
            return Optional.of(d.veri);
        }
        return Optional.empty();
    }

    /**
     * Elemanları sıralı hâlde yeni bir listeye ekler (eklemeli sıralama), asıl liste değişmez.
     * Eşit elemanlar eklenme sırasını korur.
     */
    public BagliListe<T> sirali(Comparator<? super T> karsilastirici) {
        BagliListe<T> sonuc = new BagliListe<>();
        for (T veri : this) {
            Dugum<T> yeni = new Dugum<>(veri);
            if (sonuc.bas == null || karsilastirici.compare(veri, sonuc.bas.veri) < 0) {
                yeni.sonraki = sonuc.bas;
                sonuc.bas = yeni;
            } else {
                Dugum<T> d = sonuc.bas;
                while (d.sonraki != null && karsilastirici.compare(d.sonraki.veri, veri) <= 0) {
                    d = d.sonraki;
                }
                yeni.sonraki = d.sonraki;
                d.sonraki = yeni;
            }
            if (yeni.sonraki == null) {
                sonuc.son = yeni;
            }
            sonuc.boyut++;
        }
        return sonuc;
    }

    public int boyut() {
        return boyut;
    }

    public boolean bosMu() {
        return boyut == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Dugum<T> sirada = bas;

            @Override
            public boolean hasNext() {
                return sirada != null;
            }

            @Override
            public T next() {
                if (sirada == null) {
                    throw new NoSuchElementException();
                }
                T veri = sirada.veri;
                sirada = sirada.sonraki;
                return veri;
            }
        };
    }
}
