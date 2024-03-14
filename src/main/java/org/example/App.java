package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    private static Connection connection;

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get("D:/Test/ParserJava/src/main/resources/connect.properties")));
        String url = properties.getProperty("url");


        try {
            connection = getConnection();

            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".ads-list-item");

            String sql = "INSERT INTO carinfo (Pret, Marca) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Element element : elements) {
                    String marca = element.select(".ads-list-item-content .ads-list-item-title").text();
                    String pret = element.select(".ads-list-item-content .ads-list-item-price").text();

                    statement.setString(1, pret);
                    statement.setString(2, marca);
                    statement.executeUpdate();
                }
            }
            System.out.println("Datele au fost extrase si salvate in baza de date!");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Crează conexiunea la baza de date
            String url = "jdbc:mysql://localhost:3306/information";
            String user = "root";
            String password = "root123";
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    private static String loadWebsiteUrl() {
        Properties properties = new Properties();
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
            return properties.getProperty("website.url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
