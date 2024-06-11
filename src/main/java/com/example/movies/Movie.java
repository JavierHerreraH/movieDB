package com.example.movies;

public class Movie {
    private Integer id;
    private String title;
    private Integer budget;
    private String homepage;
    private String overview;
    private Double popularity;
    private String release_date;
    private Integer revenue;
    private Integer runtime;
    private String movie_status;
    private String tagline;
    private Double vote_average;
    private Integer vote_count;

    // Getters and setters
    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public Integer getBudget() {return budget;}

    public void setBudget(Integer budget) {this.budget = budget;}

    public String getHomepage() {return homepage;}

    public void setHomepage(String homepage) {this.homepage = homepage;}

    public String getOverview() {return overview;}

    public void setOverview(String overview) {this.overview = overview;}

    public Double getPopularity() {return popularity;}

    public void setPopularity(Double popularity) {this.popularity = popularity;}

    public String getRelease_date() {return release_date;}

    public void setRelease_date(String release_date) {this.release_date = release_date;}

    public Integer getRevenue() {return revenue;}

    public void setRevenue(Integer revenue) {this.revenue = revenue;}

    public Integer getRuntime() {return runtime;}

    public void setRuntime(Integer runtime) {this.runtime = runtime;}

    public String getMovie_status() {return movie_status;}

    public void setMovie_status(String  movie_status) {this.movie_status = movie_status;}

    public String getTagline() {return tagline;}

    public void setTagline(String tagline) {this.tagline = tagline;}

    public Double getVote_average() {return vote_average;}

    public void setVote_average(Double vote_avg) {this.vote_average = vote_avg;}

    public Integer getVote_count() {return vote_count;}

    public void setVote_count(Integer vote_count) {this.vote_count = vote_count;}

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
                ", votes_avg='" + vote_average + '\'' +
                ", vote_count='" + vote_count + '\'' +
                '}';
    }
}
