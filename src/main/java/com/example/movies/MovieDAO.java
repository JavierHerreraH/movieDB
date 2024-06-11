package com.example.movies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO {
    private Connection connection;

    public MovieDAO(Connection connection) {
        this.connection = connection;
    }

    // R01. Crear una película
    public Movie save(Movie movie) {
        String sql = movie.getId() == null
                ? "INSERT INTO movie (title, budget, homepage, overview, popularity, release_date, revenue, runtime, movie_status, tagline, vote_average, vote_count) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
                : "UPDATE movie SET title = ?, budget = ?, homepage = ?, overview = ?, popularity = ?, release_date = ?, revenue = ?, runtime = ?, movie_status = ?, tagline = ?, vote_average = ?, vote_count = ? WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, movie.getTitle());
            statement.setInt(2, movie.getBudget());
            statement.setString(3, movie.getHomepage());
            statement.setString(4, movie.getOverview());
            statement.setDouble(5, movie.getPopularity());
            statement.setDate(6, Date.valueOf(movie.getRelease_date()));
            statement.setInt(7, movie.getRevenue());
            statement.setInt(8, movie.getRuntime());
            statement.setString(9, movie.getMovie_status());
            statement.setString(10, movie.getTagline());
            statement.setDouble(11, movie.getVote_average());
            statement.setInt(12, movie.getVote_count());
            if (movie.getId() == null) {
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movie.setId(generatedKeys.getInt(1));
                    }
                }
            } else {
                statement.setInt(13, movie.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movie;
    }

    // R02.
    public Optional<Movie> findById(int id) {
        String sql = "SELECT * FROM movie WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    return Optional.of(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM movie WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}