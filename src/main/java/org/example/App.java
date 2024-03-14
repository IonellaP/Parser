package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get("D:/Test/ParserJava/src/main/resources/connect.properties")));
        String url = properties.getProperty("url");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/information", "root", "root123")) {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".ads-list-photo-item");

            String sql = "INSERT INTO carinf (Pret, Marca) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Element element : elements) {
                    String marca = element.select(".ads-list-photo-item .ads-list-photo-item-title").text();
                    String pret = element.select(".ads-list-photo-item .ads-list-photo-item-price").text();

                    statement.setString(1, pret);
                    statement.setString(2, marca);

                    // Execută interogarea SQL
                    statement.executeUpdate();
                }
            }
            System.out.println("Datele au fost extrase și salvate cu succes în baza de date!");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
