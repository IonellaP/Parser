package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    private static App instance = null;

    private App() {}

    public static synchronized App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public void parseAndStoreCarData(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements carElements = doc.select(".ads-list-item-content");
            List<Car> cars = new ArrayList<>();

            for (Element carElement : carElements) {
                String brand = carElement.selectFirst(".ads-list-item-title").text();
                String price = carElement.selectFirst(".ads-list-item-price").text();
                cars.add(new Car(brand, price));
            }

            Connection connection = DatabaseConnection.getConnection();
            String insertQuery = "INSERT INTO car_table (Pret, Marca) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                for (Car car : cars) {
                    preparedStatement.setString(1, car.getBrand());
                    preparedStatement.setString(2, car.getPrice());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get("D:/Test/ParserJava/src/main/resources/connect.properties")));
        String url = properties.getProperty("url");
        App app = App.getInstance();
        app.parseAndStoreCarData(url);
    }

    static class Car {
        private String brand;
        private String price;

        public Car(String brand, String price) {
            this.brand = brand;
            this.price = price;
        }

        public String getBrand() {
            return brand;
        }

        public String getPrice() {
            return price;
        }
    }

    private static final class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/information";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "root123";

        private static Connection connection = null;

        static {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private DatabaseConnection() {}

        public static Connection getConnection() {
            return connection;
        }
    }
}
