package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
    public static void main(String[] args) {
        String url = "https://999.md/ro/list/transport/cars";

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Document document = Jsoup.connect(url).get();
                Elements masini = document.select("li.ads-list-photo-item");
                BufferedWriter writer = new BufferedWriter(new FileWriter("D:/Test/ParserJava/masini.txt"));

                for (Element masina : masini) {
                    String pret = masina.select("div.ads-list-photo-item-price").text().trim();
                    String marca = masina.select("div.ads-list-photo-item-title").text().trim();

                    writer.write("Pret: " + pret + ", Marca: " + marca +  "\n");
                }

                writer.close();

            } else {
                System.out.println("Eroare la efectuarea cererii HTTP." + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
