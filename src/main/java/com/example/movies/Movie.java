package com.example.movies;

public class Movie {
    private Integer id;
    private String title;
    private int budget;
    private String homepage;
    private String overview;
    private double popularity;
    private String release_date;
    private int revenue;
    private int runtime;
    private String movie_status;
    private String tagline;
    private double votes_avg;
    private int votes_count;

    // Getters and setters
    public Integer getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public int getBudget() {return budget;}

    public void setBudget(int budget) {this.budget = budget;}

    public String getHomepage() {return homepage;}

    public void setHomepage(String homepage) {this.homepage = homepage;}

    public String getOverview() {return overview;}

    public void setOverview(String overview) {this.overview = overview;}

    public double getPopularity() {return popularity;}

    public void setPopularity(double popularity) {this.popularity = popularity;}

    public String getRelease_date() {return release_date;}

    public void setRelease_date(String release_date) {this.release_date = release_date;}

    public int getRevenue() {return revenue;}

    public void setRevenue(int revenue) {this.revenue = revenue;}

    public int getRuntime() {return runtime;}

    public void setRuntime(int runtime) {this.runtime = runtime;}

    public String getMovie_status() {return movie_status;}

    public void setMovie_status(String  movie_status) {this.movie_status = movie_status;}

    public String getTagline() {return tagline;}

    public void setTagline(String tagline) {this.tagline = tagline;}

    public double getVotes_avg() {return votes_avg;}

    public void setVotes_avg(double votes_avg) {this.votes_avg = votes_avg;}

    public int getVotes_count() {return votes_count;}

    public void setVotes_count(int votes_count) {this.votes_count = votes_count;}

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", budget='" + budget + '\'' +
                ", homepage='" + homepage + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity='" + popularity + '\'' +
                ", release_date='" + release_date + '\'' +
                ", revenue='" + revenue + '\'' +
                ", runtime='" + runtime + '\'' +
                ", movie_status='" + movie_status + '\'' +
                ", tagline='" + tagline + '\'' +
                ", votes_avg='" + votes_avg + '\'' +
                ", votes_count='" + votes_count + '\'' +
                '}';
    }
}
