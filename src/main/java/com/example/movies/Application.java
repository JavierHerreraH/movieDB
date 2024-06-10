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
        Integer budget = null;
        String homepage = null;
        String overview = null;
        Double popularity = null;
        String release_date = null;
        Integer revenue = null;
        Integer runtime = null;
        String movie_status = null;
        String tagline = null;
        Double votes_avg = null;
        Integer votes_count = null;



        for (int i = 2; i < args.length; i++) {
            String option = args[i];
            String val = option.contains("=")
                    ? option.split("=")[1]
                    : i + 1 < args.length ? args[i + 1] : null;
            if (option.startsWith("--id")) {
                id = Integer.parseInt(val);
            } else if (option.startsWith("--title")) {
                title = val;
            } else if (option.startsWith("--budget")) {
                budget = Integer.getInteger(val);
            } else if (option.startsWith("--homepage")) {
                homepage = val;
            } else if (option.startsWith("--overview")) {
                overview = val;
            } else if (option.startsWith("--popularity")) {
                popularity = Double.parseDouble(val);
            } else if (option.startsWith("--release_date")) {
                release_date = val;
            } else if (option.startsWith("--revenue")) {
                revenue = Integer.parseInt(val);
            } else if (option.startsWith("--runtime")) {
                runtime = Integer.parseInt(val);
            } else if (option.startsWith("--movie_status")) {
                movie_status = val;
            } else if (option.startsWith("--tagline")) {
                tagline = val;
            } else if (option.startsWith("--votes_avg")) {
                votes_avg = Double.parseDouble(val);
            } else if (option.startsWith("--votes_count")) {
                votes_count = Integer.parseInt(val);
            }

        }

        if (subcommand.equals("add")) {
            addMovie(title, budget, homepage, overview, popularity, release_date, revenue, runtime, movie_status, tagline, votes_avg, votes_count);
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

    private static void addMovie(String title, Integer budget, String year, String director, Double popularity, String producer, Integer revenue, Integer runtime, String movie_status, String tagline, Double votes_avg, Integer votes_count) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setRelease_date(year);
        movie = movieDAO.save(movie);
        System.out.println(movie);
    }


    private static void printHelp() {
        System.out.println(
                "Usage: app <command> [options]\n" +
                        "Commands:\n" +
                        "  person add    --name <name>            Adds a person\n" +
                        "  person get    --id <id>                Shows a person\n" +
                        "  person update --id <id> --name <name>  Updates a person\n" +
                        "  person delete --id <id>                Deletes a person\n" +
                        "  movie add    --title <title> --budget <budget> --homepage <homepage> --overview <overview> --popularity <popularity> --release_date <release_date> --revenue <revenue> --runtime <runtime> --movie_status <movie_status> --tagline <tagline> --votes_avg <votes_avg> --votes_count <votes_count> Adds a movie\n" +
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
