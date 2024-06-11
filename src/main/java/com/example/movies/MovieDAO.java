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

    // R01. Crear una película - R03. L'usuari pot actualitzar els detalls d'una pel·lícula
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
            statement.setLong(7, movie.getRevenue());
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

    // R02. L'usuari pot consultar els detalls d'una pel·lícula (tots els atributs propis de la pel·lícula, així com director, companyia productora, gèneres, paraules clau, llenguatges, país de producció)
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
// R04. L'usuari pot eliminar una pel·lícula
    public void deleteById(int id) {
        String sql = "DELETE FROM movie WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // - R05. L'usuari pot consultar els actors (cast) d'una pel·lícula donada (mostra el nom de cada actor i el personatge que ha interpretat)
    public List<MovieCast> getCastByIdMovie(int id) {
        String sql = "SELECT character_name, person_name FROM movie_cast INNER JOIN person ON movie_cast.person_id = person.person_id WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List <MovieCast>casts=new ArrayList<>();
                while (resultSet.next()) {
                    MovieCast movieCast = new MovieCast();
                    movieCast.setCharacter_name(resultSet.getString("character_name"));
                    Person person = new Person();
                    person.setName(resultSet.getString("person_name"));
                    movieCast.setPerson(person);
                    casts.add(movieCast);
                }
                return casts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //R06. L'usuari pot consultar qui ha fet feina (crew) en la producció d'una pel·lícula donada (mostra el nom de cada persona, el departament i la feina que ha fet)

    public List<MovieCrew> getCrewByIdMovie(int id) {
        String sql = "SELECT department_name, person_name, job FROM movie_crew INNER JOIN person ON movie_crew.person_id = person.person_id INNER JOIN department ON movie_crew.department_id = department.department_id WHERE movie_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List <MovieCrew>casts=new ArrayList<>();
                while (resultSet.next()) {
                    MovieCrew movieCrew = new MovieCrew();
                    movieCrew.setDepartment_name(resultSet.getString("department_name"));
                    movieCrew.setJob(resultSet.getString("job"));
                    movieCrew.setPerson_name(resultSet.getString("person_name"));
                    casts.add(movieCrew);
                }
                return casts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
//R07. L'usuari pot cercar pel·lícules per director (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByDirector(String director) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_crew ON  movie.movie_id  = movie_crew.movie_id INNER JOIN person ON movie_crew.person_id = person.person_id  WHERE  job = 'Director' AND person_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, director);
            try (ResultSet resultSet = statement.executeQuery()) {
                List <Movie>movies=new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movies.add(movie);
                }
                return movies;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
   //R08. L'usuari pot cercar pel·lícules per actors (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByActor(String actor) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_cast ON  movie.movie_id  = movie_cast.movie_id INNER JOIN person ON movie_cast.person_id = person.person_id  WHERE movie_cast.person_id is NOT NULL AND person_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, actor);
            try (ResultSet resultSet = statement.executeQuery()) {
                List <Movie>movies=new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movies.add(movie);
                }
                return movies;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //R09. L'usuari pot cercar pel·lícules per gèneres (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByGenre(String genre) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_genres ON  movie.movie_id  = movie_genres.movie_id INNER JOIN genre ON movie_genres.genre_id = genre.genre_id  WHERE genre_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre);
            try (ResultSet resultSet = statement.executeQuery()) {
                List <Movie>movies=new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movies.add(movie);
                }
                return movies;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
   // R13. L'usuari pot veure el ranking de les 10 pel·lícules que més han recaptat (mostra id, títol, any i recaptació)
  public List<Movie> top10RevenueMovie() {
      String sql = "SELECT movie_id, title, release_date, revenue FROM movie ORDER BY revenue DESC LIMIT 10 ";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {

          try (ResultSet resultSet = statement.executeQuery()) {
              List<Movie> movies = new ArrayList<>();
              while (resultSet.next()) {
                  Movie movie = new Movie();
                  movie.setId(resultSet.getInt("movie_id"));
                  movie.setTitle(resultSet.getString("title"));
                  movie.setRelease_date(resultSet.getString("release_date"));
                  movie.setRevenue(resultSet.getLong("revenue"));
                  movies.add(movie);
              }
              return movies;
          } catch (Exception e){
              e.printStackTrace();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

      return null;
  }
    //  R14. L'usuari pot veure el ranking de les 10 pel·lícules amb major pressupost (mostra id, títol, any i pressupost)
    public List<Movie> top10BudgetMovie() {
        String sql = "SELECT movie_id, title, release_date, budget FROM movie ORDER BY budget DESC LIMIT 10 ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Movie> movies = new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movie.setBudget(resultSet.getInt("budget"));
                    movies.add(movie);
                }
                return movies;
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
   // R16. L'usuari pot veure el ranking de les 10 pel·lícules millor puntuades, només entre les que tenen més de 1000 vots (mostra id, títol, any i puntuació)
    public List<Movie> top10RatingMovie() {
        String sql = "SELECT movie_id, title, release_date, vote_average FROM movie WHERE vote_count > 1000 ORDER BY vote_average DESC LIMIT 10  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Movie> movies = new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movie.setVote_average(resultSet.getDouble("vote_average"));
                    movies.add(movie);
                }
                return movies;
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}