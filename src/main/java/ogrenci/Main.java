package ogrenci;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var cikis = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var giris = new Scanner(System.in, StandardCharsets.UTF_8);
        new OgrenciUygulamasi(new Sinif(), giris, cikis).calistir();
    }
}
