package com.example.movies;

import java.util.List;
import java.util.NoSuchElementException;

public class Application {
    static MovieDAO movieDAO;
    static PersonDAO personDAO;
    public static int getAdd;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printHelp();
                return;
            }
        }

        movieDAO = new MovieDAO(Database.getConnection());
        personDAO = new PersonDAO(Database.getConnection());

        String command = args[0];
        if (command.equals("person")) {
            handlePersonCommand(args);
        } else if (command.equals("movie")) {
            handleMovieCommand(args);
        }
    }

    private static void handlePersonCommand(String[] args) {
        String subcommand = args[1];
        Integer id = null;
        String name = null;

        for (int i = 2; i < args.length; i++) {
            String option = args[i];
            String value = option.contains("=")
                    ? option.split("=")[1]
                    : i + 1 < args.length ? args[i + 1] : null;
            if (option.startsWith("--id")) {
                id = Integer.parseInt(value);
            } else if (option.startsWith("--name")) {
                name = value;
            }
        }

        if (subcommand.equals("add")) {
            addPerson(name);
        } else if (subcommand.equals("get")) {
            getPerson(id);
        } else if (subcommand.equals("update")) {
            updatePerson(id, name);
        } else if (subcommand.equals("delete")) {
            deletePerson(id);
        }
    }

    private static void handleMovieCommand(String[] args) {
        String subcommand = args[1];
        Movie movie = new Movie();
        int topSearch = 0;
        boolean isAddCommand = subcommand.equals("add");

        for (int i = 2; i < args.length; i++) {
            String option = args[i];
            String val = option.contains("=")
                    ? option.split("=")[1]
                    : i + 1 < args.length ? args[i + 1] : null;
            if (option.startsWith("--id")) {
                movie.setId(Integer.parseInt(val));
            } else if (option.startsWith("--title")) {
                movie.setTitle(val);
            } else if (option.startsWith("--budget")) {
                if (isAddCommand) {
                    movie.setBudget(Integer.parseInt(val));
                } else {
                    topSearch = 2;
                }
            } else if (option.startsWith("--homepage")) {
                movie.setHomepage(val);
            } else if (option.startsWith("--overview")) {
                movie.setOverview(val);
            } else if (option.startsWith("--popularity")) {
                if (isAddCommand) {
                    movie.setPopularity(Double.parseDouble(val));
                } else {
                    topSearch = 4;
                }
            } else if (option.startsWith("--year")) {
                movie.setRelease_date(val);
            } else if (option.startsWith("--revenue")) {
                if (isAddCommand) {
                    movie.setRevenue(Long.parseLong(val));
                } else {
                    topSearch = 1;
                }
            } else if (option.startsWith("--runtime")) {
                movie.setRuntime(Integer.parseInt(val));
            } else if (option.startsWith("--movie_status")) {
                movie.setMovie_status(val);
            } else if (option.startsWith("--tagline")) {
                movie.setTagline(val);
            } else if (option.startsWith("--rating")) {
                if (isAddCommand) {
                    movie.setVote_average(Double.parseDouble(val));
                } else {
                    topSearch = 3;
                }
            } else if (option.startsWith("--vote_count")) {
                movie.setVote_count(Integer.parseInt(val));
            } else if (option.startsWith("--director")) {
                movie.setDirector(val);
            } else if (option.startsWith("--actor")) {
                movie.setActor(val);
            } else if (option.startsWith("--genre")) {
                movie.setGenre(val);
            } else if (option.startsWith("--keyword")) {
                movie.setKeyword(val);
            } else if (option.startsWith("--production")) {
                movie.setProduction(val);
            } else if (option.startsWith("--order")) {
                movie.setOrder(val);
            } else if (option.startsWith("--asc")) {
                movie.setAscendent(true);
            } else if (option.startsWith("--desc")) {
                movie.setAscendent(false);
            } else if (option.startsWith("--page")) {
                movie.setPage(Integer.parseInt(val));
            } else if (option.startsWith("--size")) {
                movie.setPageSize(Integer.parseInt(val));
            }
        }

        if (subcommand.equals("add")) {
            addMovie(movie);
        } else if (subcommand.equals("get")) {
            if (!movie.getDirector().isEmpty()) {
                getMovieDirector(movie.getDirector(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else if (!movie.getActor().isEmpty()) {
                getMovieActor(movie.getActor(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else if (!movie.getGenre().isEmpty()) {
                getMovieGenre(movie.getGenre(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else if (!movie.getRelease_date().isEmpty()) {
                getMovieYear(movie.getRelease_date(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else if (!movie.getKeyword().isEmpty()) {
                getMovieKeyword(movie.getKeyword(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else if (!movie.getProduction().isEmpty()) {
                getMovieProductions(movie.getProduction(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            } else {
                getMovie(movie.getId(), movie.getOrder(), movie.getAscendent(), movie.getPage(), movie.getPageSize());
            }
        } else if (subcommand.equals("update")) {
            updateMovie(movie);
        } else if (subcommand.equals("delete")) {
            deleteMovie(movie.getId());
        } else if (subcommand.equals("cast")) {
            getCast(movie.getId());
        } else if (subcommand.equals("crew")) {
            getCrew(movie.getId());
        } else if (subcommand.equals("top")) {
            if (topSearch == 1) {
                getTopMovieRevenue();
            } else if (topSearch == 2) {
                getTopMovieBudget();
            } else if (topSearch == 3) {
                getTopMovieRating();
            } else if (topSearch == 4) {
                getTopMoviePopularity();
            }
        }
    }


    private static void addPerson(String name) {
        Person person = new Person();
        person.setName(name);
        person = personDAO.save(person);
        System.out.println(person);
    }

    private static void deletePerson(Integer id) {
        personDAO.deleteById(id);
    }

    private static void getPerson(int id) {
        try {
            Person person = personDAO.findById(id).get();
            System.out.println(person);
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such person found");
        }
    }

    private static void updatePerson(Integer id, String name) {
        try {
            Person person = personDAO.findById(id).get();
            person.setName(name);
            person = personDAO.save(person);
            System.out.println(person);
        } catch (NoSuchElementException e) {
            System.err.println("Error: No such person found");
        }
    }

    private static void addMovie(Movie movie) {
        movie = movieDAO.save(movie);
        System.out.println(movie);
    }

    private static void updateMovie(Movie updatedMovie) {
        try {
            Movie movie = movieDAO.findById(updatedMovie.getId(), "", false, -1, -1).get();
            movie.setTitle(updatedMovie.getTitle() != null ? updatedMovie.getTitle() : movie.getTitle());
            movie.setBudget(updatedMovie.getBudget() != null ? updatedMovie.getBudget() : movie.getBudget());
            movie.setHomepage(updatedMovie.getHomepage() != null ? updatedMovie.getHomepage() : movie.getHomepage());
            movie.setOverview(updatedMovie.getOverview() != null ? updatedMovie.getOverview() : movie.getOverview());
            movie.setPopularity(updatedMovie.getPopularity() != null ? updatedMovie.getPopularity() : movie.getPopularity());
            movie.setRelease_date(updatedMovie.getRelease_date() != null ? updatedMovie.getRelease_date() : movie.getRelease_date());
            movie.setRevenue(updatedMovie.getRevenue() != null ? updatedMovie.getRevenue() : movie.getRevenue());
            movie.setRuntime(updatedMovie.getRuntime() != null ? updatedMovie.getRuntime() : movie.getRuntime());
            movie.setMovie_status(updatedMovie.getMovie_status() != null ? updatedMovie.getMovie_status() : movie.getMovie_status());
            movie.setTagline(updatedMovie.getTagline() != null ? updatedMovie.getTagline() : movie.getTagline());
            movie.setVote_average(updatedMovie.getVote_average() != null ? updatedMovie.getVote_average() : movie.getVote_average());
            movie.setVote_count(updatedMovie.getVote_count() != null ? updatedMovie.getVote_count() : movie.getVote_count());
            movie = movieDAO.save(movie);
            System.out.println(movie);
        } catch (NoSuchElementException e) {
            System.err.println("Error: No such person found");
        }
    }

    private static void getMovie(int id, String order, boolean ascendent, int page, int pageSize) {
        try {
            Movie movie = movieDAO.findById(id, order, ascendent, page, pageSize).get();
            System.out.println(movie);
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void deleteMovie(Integer id) {
        movieDAO.deleteById(id);
    }

    private static void getCast(int id) {
        try {
            List<MovieCast> movieCasts = movieDAO.getCastByIdMovie(id);
            for (MovieCast c : movieCasts) {
                System.out.println(c);
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such cast found");
        }
    }

    private static void getCrew(Integer id) {
        try {
            List<MovieCrew> movieCrews = movieDAO.getCrewByIdMovie(id);
            for (MovieCrew c : movieCrews) {
                System.out.println(c);
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such crew found");
        }
    }

    private static void getMovieDirector(String director, String order, Boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByDirector(director, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getMovieYear(String release_date, String order, boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByReleaseDate(release_date, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getMovieProductions(String production, String order, boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByProduction(production, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getMovieKeyword(String keyword, String order, boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByKeyword(keyword, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getMovieActor(String actor, String order, boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByActor(actor, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getMovieGenre(String genre, String order, boolean ascendent, int page, int pageSize) {
        try {
            List<Movie> movie = movieDAO.findByGenre(genre, order, ascendent, page, pageSize);
            for (Movie c : movie) {
                System.out.println(c.resumen());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getTopMovieRevenue() {
        try {
            List<Movie> movie = movieDAO.top10RevenueMovie();
            for (Movie c : movie) {
                System.out.println(c.resumenRevenue());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }
    }

    private static void getTopMovieBudget() {
        try {
            List<Movie> movie = movieDAO.top10BudgetMovie();
            for (Movie c : movie) {
                System.out.println(c.resumenBudget());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }

    }

    private static void getTopMoviePopularity() {
        try {
            List<Movie> movie = movieDAO.top10PopularMovie();
            for (Movie c : movie) {
                System.out.println(c.resumenVoteAverage());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }

    }

    private static void getTopMovieRating() {
        try {
            List<Movie> movie = movieDAO.top10RatingMovie();
            for (Movie c : movie) {
                System.out.println(c.resumenVoteAverage());
            }
        } catch (NoSuchElementException e) {
            System.err.println(e);
            System.err.println("Error: No such movie found");
        }

    }


    private static void printHelp() {
        System.out.println(
                "Usage: app <command> [options]\n" +
                        "Commands:\n" +
                        "  person add    --name <name>            Adds a person\n" +
                        "  person get    --id <id>                Shows a person\n" +
                        "  person update --id <id> --name <name>  Updates a person\n" +
                        "  person delete --id <id>                Deletes a person\n" +
                        "  movie add    --title <title> --budget <budget> --homepage <homepage> --overview <overview> --popularity <popularity> --release_date <release_date> --revenue <revenue> --runtime <runtime> --movie_status <movie_status> --tagline <tagline> --vote_average <vote_average> --vote_count <vote_count> Adds a movie\n" +
                        "  movie get    --id <id> || --director <name_director> || --genre <genre> || --actor <actor> || --year <year> Shows movies\n" +
                        "  movie update --id <id> --title <title> --year <year> --director <director> --producer <producer> Updates a movie\n" +
                        "  movie delete --id <id>                Deletes a movie\n" +
                        "  movie cast   --id <id>                Shows the cast of a movie\n" +
                        "  movie crew   --id <id>                Shows the crew of a movie\n" +
                        "  movie top    --revenue || --budget || --popularity || --rating  \n" +
                        "  --page <num>                          The number of page  \n" +
                        "  --size <num>                          Size of the page  \n" +
                        "  --order <title,release_date,popularity,vote_average,vote_count>   --asc || --desc  Can order by title,release_date,popularity,vote_average,vote_count  like ascendent || descendent \n"
        );
    }

    private static void printUsage() {
        System.err.println(
                "Usage: app [person [add | get | update | delete]] [options]\n" +
                        "Usage: app [movie [add | get | update | delete | search | cast | crew | top]] [options]"
        );
    }
}


