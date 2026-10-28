# Öğrenci Yönetim Sistemi

[![Testler](https://github.com/MERCANX25/ogrenci-yonetim-sistemi/actions/workflows/test.yml/badge.svg)](https://github.com/MERCANX25/ogrenci-yonetim-sistemi/actions/workflows/test.yml)

Öğrencileri ve notlarını **kendi yazdığım tek yönlü bağlı listede** tutan Java konsol uygulaması. Veri yapıları dersinde bağlı listeyi uygulamak için yaptım. Öğrenci ekleme, arama, not güncelleme, silme, başarı sıralaması ve sınıf istatistikleri var.

```
Seçiminiz: 3
--------------------------------------------------------------
Ad Soyad                   Vize  Final   Ortalama   Durum
--------------------------------------------------------------
Zeynep Kaya                  90     95      93,00   Geçti
Ayşe Yılmaz                  60     80      72,00   Geçti
Mehmet Demir                 35     45      41,00   Kaldı
--------------------------------------------------------------
Seçiminiz: 7
Öğrenci sayısı    : 3 (2 geçti, 1 kaldı)
Sınıf ortalaması  : 68,67
En yüksek         : 93,00
En düşük          : 41,00
```

Ortalama, vizenin %40'ı ile finalin %60'ının toplamı. 50 ve üzeri geçer.

## Bağlı liste

[`BagliListe<T>`](src/main/java/ogrenci/BagliListe.java) Java'nın hazır koleksiyonlarını kullanmadan yazıldı:

| İşlem | Nasıl | Süre |
|---|---|---|
| `ekle` | Son düğüm de tutulduğu için listeyi dolaşmadan sona eklenir | O(1) |
| `bul` | Baştan başlayıp koşula uyan ilk düğüme kadar ilerler | O(n) |
| `sil` | Önceki düğümün `sonraki` bağlantısı silinen düğümün arkasına bağlanır; baş ve son düğüm ayrıca ele alınır | O(n) |
| `sirali` | Eklemeli sıralamayla yeni bir liste kurar, asıl liste değişmez; eşit elemanların sırası korunur | O(n²) |

Liste generic ve `Iterable` olduğu için `for (Ogrenci o : liste)` ile dolaşılabiliyor.

## Çalıştırma

Gereksinim: Java 21 ve Maven.

```bash
mvn package
java -jar target/ogrenci-yonetim-sistemi-1.0.0.jar
```

Testler:

```bash
mvn test
```

## Yapı

```
src/main/java/ogrenci/
  BagliListe.java          Tek yönlü bağlı liste (ekle, bul, sil, sıralı kopya)
  Ogrenci.java             Notlar, ortalama, geçme durumu, ad karşılaştırma
  Sinif.java               Öğrenci listesi, tekrar kontrolü, sıralama, istatistik
  OgrenciUygulamasi.java   Konsol menüsü
  Main.java                Giriş noktası
src/test/java/ogrenci/     30 JUnit 5 testi
```

## İlk sürüme göre neler değişti

İlk sürüm ekleme, listeleme ve silme yapan tek dosyalık bir programdı. Yeniden düzenlerken:

- **Bağlı liste ayrı ve generic bir sınıf oldu.** Son düğüm tutulduğu için sona ekleme artık listeyi baştan dolaşmıyor.
- **Hatalı girişler:** Menüde ya da notlarda harf girilince program çöküyordu, artık tekrar soruyor. Notlar 0–100 arasında olmak zorunda, boş ad kabul edilmiyor.
- **Aynı öğrenci iki kez eklenemiyor.** Ad karşılaştırması Türkçe büyük/küçük harf kurallarına göre yapılıyor ("İREM IŞIK" ile "irem ışık" aynı kişi), fazla boşluklar önemsenmiyor.
- **Yeni özellikler:** Öğrenci arama, not güncelleme, başarı sıralaması ve sınıf istatistikleri.
- **Maven projesi ve testler:** Bağlı liste, not hesabı ve menü akışı için 30 test var, GitHub Actions ile her gönderimde çalışıyor.
