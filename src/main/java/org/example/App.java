package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        String url = "https://999.md/ro/85852890";
        try {
            Document document = Jsoup.connect(url).get();

            Element carElement = document.selectFirst(".adPage__content__features");
            Element carPrice = document.selectFirst(".adPage__aside__price");

            String marca = carElement.select(".adPage__content__features__value").text();
            String model = carElement.select(".adPage__content__features__value").text();
            String an = carElement.select(".adPage__content__features__value").text();
            String pret = carPrice.select(".adPage__content__price-feature__prices__price.is-main").text();

            BufferedWriter writer = new BufferedWriter(new FileWriter("D:/Test/ParserJava/masina.txt"));

            writer.write("Marca: " + marca + "\n");
            //writer.write("Model: " + model + "\n");
            // writer.write("An: " + an + "\n");
            writer.write("Pret: " + pret + "\n");

            writer.close();
            System.out.println("Informatiile au fost adaugate in fisierul masina.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
