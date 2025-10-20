import java.util.Scanner;

class Node {
    String adSoyad;
    int vize;
    int finalnot;
    double ortalama;
    boolean gectimi;
    Node next;

    public Node(String adSoyad, int vize, int finalnot) {
        this.adSoyad = adSoyad;
        this.vize = vize;
        this.finalnot = finalnot;
        this.ortalama = (vize * 0.4) + (finalnot * 0.6);
        this.gectimi = (ortalama >= 50);
        this.next = null;
    }
}

class ogrSis{

    // Öğrenci eklemek için
    public static Node ogrenciEkle(Node head, Scanner input) {
        System.out.print("Ad-Soyad: ");
        String adSoyad = input.nextLine(); // girilen adı adsoyada atadık

        System.out.print("Vize Notu: ");
        int vize = input.nextInt();

        System.out.print("Final Notu: ");
        int finalnot = input.nextInt();
        input.nextLine();

        Node yeniogrenci = new Node(adSoyad, vize, finalnot);

        if (head == null) { // liste boşsa
            head = yeniogrenci;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = yeniogrenci;
        }

        System.out.println("Öğrenci eklendi.");
        return head;
    }

    // Öğrenci silme
    public static Node ogrenciSil(Node head, Scanner input) {
        if (head == null) { // liste boş
            System.out.println("Öğrenci yok.");
            return head;
        }

        System.out.print("Silinecek öğrencinin Ad-Soyad bilgisini giriniz: ");
        String silinecekAd = input.nextLine();

        Node current = head;
        Node onceki = null;

        if (current!= null && current.adSoyad.equalsIgnoreCase(silinecekAd)) {
            head = current.next;
            System.out.println(silinecekAd + " adlı öğrenci silindi.");
            return head;
        }

        while (current != null && !current.adSoyad.equalsIgnoreCase(silinecekAd)) {
            onceki = current;
            current =  current.next;
        }

        if (current == null) {
            System.out.println(silinecekAd + " adlı öğrenci bulunamadı.");
            return head;
        }

        onceki.next = current.next;
        System.out.println(silinecekAd + " adlı öğrenci silindi.");
        return head;
    }

    // Tablo yazdırma
    public static void tabloYazdir(Node head) {
        if (head == null) {
            System.out.println("Henüz öğrenci yok!");
            return;
        }

        System.out.println("\n-----------------------------------------------");
        System.out.printf("%-20s %-10s %-10s %-10s %-10s\n",
                "Ad Soyad", "Vize", "Final", "Ortalama", "Durum");
        System.out.println("-----------------------------------------------");

        Node temp = head;
        while (temp != null) {
            System.out.printf("%-20s %-10d %-10d %-10.2f %-10s\n",
                    // s string 20 karakterlik alan boşluklar için.
                    // d int f double demektir.

                    temp.adSoyad,
                    temp.vize,
                    temp.finalnot,
                    temp.ortalama,
                    temp.gectimi ? "Geçti ✅" : "Kaldı ❌");
            temp = temp.next;
        }
        System.out.println("-----------------------------------------------");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Node head = null;
        int secim;

        do {
            System.out.println("\n===== ÖĞRENCİ YÖNETİM MENÜSÜ =====");
            System.out.println("1. Yeni Öğrenci Ekle");
            System.out.println("2. Tüm Öğrencileri Listele");
            System.out.println("3. Öğrenci Sil");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminiz: ");
            secim = input.nextInt();
            input.nextLine(); // buffer temizliği

            switch (secim) {
                case 1:
                    head = ogrenciEkle(head, input);
                    break;
                case 2:
                    tabloYazdir(head);
                    break;
                case 3:
                    head = ogrenciSil(head, input);
                    break;
                case 4:
                    System.out.println("Programdan çıkılıyor...");
                    break;
                default:
                    System.out.println("Geçersiz seçim! 1-4 arası girin.");
            }

        } while (secim != 4);
    }
}
