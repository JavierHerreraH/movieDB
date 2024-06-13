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
            statement.setObject(2, movie.getBudget(), Types.INTEGER);
            statement.setString(3, movie.getHomepage());
            statement.setString(4, movie.getOverview());
            statement.setObject(5, movie.getPopularity(), Types.DOUBLE);
            statement.setObject(6, movie.getRelease_date() != null ? Date.valueOf(movie.getRelease_date()) : null, Types.DATE);
            statement.setObject(7, movie.getRevenue(), Types.BIGINT);
            statement.setObject(8, movie.getRuntime(), Types.INTEGER);
            statement.setString(9, movie.getMovie_status());
            statement.setString(10, movie.getTagline());
            statement.setObject(11, movie.getVote_average(), Types.DOUBLE);
            statement.setObject(12, movie.getVote_count(), Types.INTEGER);

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
    public Optional<Movie> findById(int id, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT * FROM movie WHERE movie_id = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        if (pageSize > 0){
            sql += " LIMIT " + pageSize;
            int n = 1;
            if (page > 0){
                n = page;
            }
            int count = (n - 1) * pageSize;
            sql += " OFFSET " + count;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setBudget(resultSet.getInt("budget"));
                    movie.setHomepage(resultSet.getString("homepage"));
                    movie.setOverview(resultSet.getString("overview"));
                    movie.setPopularity(resultSet.getDouble("popularity"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movie.setRevenue(resultSet.getLong("revenue"));
                    movie.setBudget(resultSet.getInt("runtime"));
                    movie.setMovie_status(resultSet.getString("movie_status"));
                    movie.setTagline(resultSet.getString("tagline"));
                    movie.setVote_average(resultSet.getDouble("vote_average"));
                    movie.setVote_count(resultSet.getInt("vote_count"));

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
                    movieCast.setName(resultSet.getString("person_name"));
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
    public List<Movie> findByDirector(String director, String order, Boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_crew ON  movie.movie_id  = movie_crew.movie_id INNER JOIN person ON movie_crew.person_id = person.person_id  WHERE  job = 'Director' AND person_name = ?";
       if(!order.isEmpty()){
           sql += " ORDER BY " + order;
           if(ascendent){
               sql += " ASC";
           } else {
               sql += " DESC";
           }
       }
       if (pageSize > 0){
           sql += " LIMIT " + pageSize;
           int n = 1;
           if (page > 0){
               n = page;
           }
           int count = (n - 1) * pageSize;
           sql += " OFFSET " + count;
       }
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
    public List<Movie> findByActor(String actor, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_cast ON  movie.movie_id  = movie_cast.movie_id INNER JOIN person ON movie_cast.person_id = person.person_id  WHERE movie_cast.person_id is NOT NULL AND person_name = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        if (pageSize > 0){
            sql += " LIMIT " + pageSize;
            int n = 1;
            if (page > 0){
                n = page;
            }
            int count = (n - 1) * pageSize;
            sql += " OFFSET " + count;
        }
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
    public List<Movie> findByGenre(String genre, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_genres ON  movie.movie_id  = movie_genres.movie_id INNER JOIN genre ON movie_genres.genre_id = genre.genre_id  WHERE genre_name = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        if (pageSize > 0){
            sql += " LIMIT " + pageSize;
            int n = 1;
            if (page > 0){
                n = page;
            }
            int count = (n - 1) * pageSize;
            sql += " OFFSET " + count;
        }
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
    //R10. L'usuari pot cercar pel·lícules per any (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByReleaseDate(String release_date, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie WHERE DATE_PART('year', release_date) = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        if (pageSize > 0){
            sql += " LIMIT " + pageSize;
            int n = 1;
            if (page > 0){
                n = page;
            }
            int count = (n - 1) * pageSize;
            sql += " OFFSET " + count;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int year = Integer.parseInt(release_date);
            statement.setInt(1, year);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Movie> movies = new ArrayList<>();
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
        } catch (NumberFormatException e) {
            // Manejo del caso en que release_date no sea un número válido
            e.printStackTrace();
        }

        return null;
    }

    //R11. L'usuari pot cercar pel·lícules per paraules clau (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByKeyword(String Keyword, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_keywords ON movie.movie_id  = movie_keywords.movie_id INNER JOIN keyword ON keyword.keyword_id = movie_keywords.keyword_id  WHERE keyword_name = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        if (pageSize > 0){
            sql += " LIMIT " + pageSize;
            int n = 1;
            if (page > 0){
                n = page;
            }
            int count = (n - 1) * pageSize;
            sql += " OFFSET " + count;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, Keyword);
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
    //R12. L'usuari pot cercar pel·lícules per companyia productora (mostra id, títol i any de la pel·lícula)
    public List<Movie> findByProduction(String production, String order, boolean ascendent, int page, int pageSize) {
        String sql = "SELECT title, movie.movie_id, release_date FROM movie INNER JOIN movie_company ON movie.movie_id  = movie_company.movie_id INNER JOIN production_company ON movie_company.company_id = production_company.company_id  WHERE company_name = ?";
        if(!order.isEmpty()){
            sql += " ORDER BY " + order;
            if(ascendent){
                sql += " ASC";
            } else {
                sql += " DESC";
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, production);
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
    //R15. L'usuari pot veure el ranking de les 10 pel·lícules més populars (mostra id, títol, any i número de vots)
    public List<Movie> top10PopularMovie() {
        String sql = "SELECT movie_id, title, release_date, popularity FROM movie ORDER BY popularity DESC LIMIT 10 ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Movie> movies = new ArrayList<>();
                while (resultSet.next()) {
                    Movie movie = new Movie();
                    movie.setId(resultSet.getInt("movie_id"));
                    movie.setTitle(resultSet.getString("title"));
                    movie.setRelease_date(resultSet.getString("release_date"));
                    movie.setPopularity(resultSet.getDouble("popularity"));
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
    //R17. L'usuari pot ordenar les pel·lícules que surten en una cerca per títol, any, popularitat, número de vots o puntuació

}