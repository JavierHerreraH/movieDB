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
                ? "INSERT INTO movie (title, year, director, producer) VALUES (?, ?, ?, ?)"
                : "UPDATE movie SET title = ?, year = ?, director = ?, producer = ? WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getYear());
            statement.setString(3, movie.getDirector());
            statement.setString(4, movie.getProducer());
            if (movie.getId() == null) {
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movie.setId(generatedKeys.getInt(1));
                    }
                }
            } else {
                statement.setInt(5, movie.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    // R02. Consultar los detalles de una película
    public Optional<Movie> findById(int id) {
        String sql = "SELECT * FROM movie WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    Date fecha = resultSet.getDate("release_date");
                    movie.setYear(fecha.toString());
                   // movie.setYear(resultSet.getInt("release_date"));
                    movie.setDirector(resultSet.getString("director"));
                    movie.setProducer(resultSet.getString("producer"));
                    // Load other movie attributes
                    return Optional.of(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // R04. Eliminar una película
    public void deleteById(int id) {
        String sql = "DELETE FROM movie WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos adicionales para requisitos
    public List<Actor> getCast(int movieId) {
        String sql = "SELECT a.name, c.character_name FROM cast c JOIN actor a ON c.actor_id = a.actor_id WHERE c.movie_id = ?";
        List<Actor> cast = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, movieId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Actor actor = new Actor();
                    actor.setName(resultSet.getString("name"));
                    actor.setCharacterName(resultSet.getString("character_name"));
                    cast.add(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cast;
    }

    public List<CrewMember> getCrew(int movieId) {
        String sql = "SELECT p.name, c.department, c.job FROM crew c JOIN person p ON c.person_id = p.person_id WHERE c.movie_id = ?";
        List<CrewMember> crew = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, movieId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CrewMember crewMember = new CrewMember();
                    crewMember.setName(resultSet.getString("name"));
                    crewMember.setDepartment(resultSet.getString("department"));
                    crewMember.setJob(resultSet.getString("job"));
                    crew.add(crewMember);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crew;
    }

    public List<Movie> findByAttribute(String attribute, String value) {
        String sql = "SELECT * FROM movie WHERE " + attribute + " LIKE ?";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + value + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setYear(resultSet.getString("release_date"));
                    // Set other attributes
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findTopMovies(String attribute, int limit) {
        String sql = "SELECT * FROM movie ORDER BY " + attribute + " DESC LIMIT ?";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setYear(resultSet.getString("release_date"));
                    // Set other attributes
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> findMovies(String attribute, String order, int limit, int offset) {
        String sql = "SELECT * FROM movie ORDER BY " + attribute + " " + order + " LIMIT ? OFFSET ?";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setYear(resultSet.getString("release_date"));
                    // Set other attributes
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
