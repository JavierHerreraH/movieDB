package com.example.movies;

import java.util.List;
import java.util.NoSuchElementException;

public class Application {
    static MovieDAO movieDAO;
    static PersonDAO personDAO;

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
        Integer id = null;
        String title = null;
        String year = null;
        String director = null;
        String producer = null;
        String attribute = null;
        String value = null;
        String order = "asc";
        int limit = 10;
        int offset = 0;

        for (int i = 2; i < args.length; i++) {
            String option = args[i];
            String val = option.contains("=")
                    ? option.split("=")[1]
                    : i + 1 < args.length ? args[i + 1] : null;
            if (option.startsWith("--id")) {
                id = Integer.parseInt(val);
            } else if (option.startsWith("--title")) {
                title = val;
            } else if (option.startsWith("--year")) {
                year = val;
            } else if (option.startsWith("--director")) {
                director = val;
            } else if (option.startsWith("--producer")) {
                producer = val;
            } else if (option.startsWith("--attribute")) {
                attribute = val;
            } else if (option.startsWith("--value")) {
                value = val;
            } else if (option.startsWith("--order")) {
                order = val;
            } else if (option.startsWith("--limit")) {
                limit = Integer.parseInt(val);
            } else if (option.startsWith("--offset")) {
                offset = Integer.parseInt(val);
            }
        }

        if (subcommand.equals("add")) {
            addMovie(title, year, director, producer);
        } else if (subcommand.equals("get")) {
            getMovie(id);
        } else if (subcommand.equals("update")) {
            updateMovie(id, title, year, director, producer);
        } else if (subcommand.equals("delete")) {
            deleteMovie(id);
        } else if (subcommand.equals("search")) {
            searchMovies(attribute, value, order, limit, offset);
        } else if (subcommand.equals("cast")) {
            getMovieCast(id);
        } else if (subcommand.equals("crew")) {
            getMovieCrew(id);
        } else if (subcommand.equals("top")) {
            getTopMovies(attribute, limit);
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

    private static void addMovie(String title, String year, String director, String producer) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setYear(year);
        movie.setDirector(director);
        movie.setProducer(producer);
        movie = movieDAO.save(movie);
        System.out.println(movie);
    }

    private static void deleteMovie(Integer id) {
        movieDAO.deleteById(id);
    }

    private static void getMovie(int id) {
        try {
            Movie movie = movieDAO.findById(id).get();
            System.out.println(movie);
        } catch (NoSuchElementException e) {
            System.err.println("Error: No such movie found");
        }
    }

    private static void updateMovie(Integer id, String title, String year, String director, String producer) {
        try {
            Movie movie = movieDAO.findById(id).get();
            movie.setTitle(title);
            movie.setYear(year);
            movie.setDirector(director);
            movie.setProducer(producer);
            movie = movieDAO.save(movie);
            System.out.println(movie);
        } catch (NoSuchElementException e) {
            System.err.println("Error: No such movie found");
        }
    }

    private static void searchMovies(String attribute, String value, String order, int limit, int offset) {
        List<Movie> movies = movieDAO.findMovies(attribute, order, limit, offset);
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    private static void getMovieCast(int id) {
        List<Actor> cast = movieDAO.getCast(id);
        for (Actor actor : cast) {
            System.out.println(actor);
        }
    }

    private static void getMovieCrew(int id) {
        List<CrewMember> crew = movieDAO.getCrew(id);
        for (CrewMember crewMember : crew) {
            System.out.println(crewMember);
        }
    }

    private static void getTopMovies(String attribute, int limit) {
        List<Movie> movies = movieDAO.findTopMovies(attribute, limit);
        for (Movie movie : movies) {
            System.out.println(movie);
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
                        "  movie add    --title <title> --year <year> --director <director> --producer <producer> Adds a movie\n" +
                        "  movie get    --id <id>                Shows a movie\n" +
                        "  movie update --id <id> --title <title> --year <year> --director <director> --producer <producer> Updates a movie\n" +
                        "  movie delete --id <id>                Deletes a movie\n" +
                        "  movie search --attribute <attribute> --value <value> --order <asc|desc> --limit <number> --offset <number> Searches for movies by attribute and value\n" +
                        "  movie cast   --id <id>                Shows the cast of a movie\n" +
                        "  movie crew   --id <id>                Shows the crew of a movie\n" +
                        "  movie top    --attribute <attribute> --limit <number> Shows top movies by attribute\n"
        );
    }

    private static void printUsage() {
        System.err.println(
                "Usage: app [person [add | get | update | delete]] [options]\n" +
                        "Usage: app [movie [add | get | update | delete | search | cast | crew | top]] [options]"
        );
    }
}
