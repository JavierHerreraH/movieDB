package com.example.movies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];
        if (command.equals("show-movies")) {
            showMovies();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: app <command> [options]");
    }

    private static void showMovies() {
        String url = System.getenv("DATASOURCE_URL");
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            String sql = "SELECT movie_id, title, release_date FROM movie LIMIT 10";
            resultSet = statement.executeQuery(sql);

            System.out.println("id, title, year");
            while (resultSet.next()) {
                int movieId = resultSet.getInt("movie_id");
                String title = resultSet.getString("title");
                int year = resultSet.getDate("release_date").toLocalDate().getYear();
                System.out.println(movieId + ", " + title + ", " + year);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
