package com.example.movies;

import java.util.Arrays;
import java.util.List;

public class Movie {
    private Integer id;
    private String title;
    private Integer budget;
    private String homepage;
    private String overview;
    private Double popularity;
    private String release_date;
    private Long revenue;
    private Integer runtime;
    private String movie_status;
    private String tagline;
    private Double vote_average;
    private Integer vote_count;
    private String director = "";
    private String actor = "";
    private String genre = "";
    private String keyword = "";
    private String production = "";
    private String order = "";
    private boolean ascendent = true;
    private int page = -1;
    private int pageSize = -1;
    public static List<String> list = Arrays.asList(new String[]{"title", "release_date", "popularity", "vote_average", "vote_count"});

    public boolean getAscendent() {
        return ascendent;
    }

    public void setAscendent(boolean ascendent) {
        this.ascendent = ascendent;
    }


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        if (list.contains(order)) {
            this.order = order;

        }
    }


    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getMovie_status() {
        return movie_status;
    }

    public void setMovie_status(String movie_status) {
        this.movie_status = movie_status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_avg) {
        this.vote_average = vote_avg;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }



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


    public String resumen() {
        return "Movie {" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                '}';
    }

    public String resumenRevenue() {
        return "Movie {" +
                "id= " + id +
                ", title= '" + title + '\'' +
                ", release_date= '" + release_date + '\'' +
                ", revenue= " + revenue + "$" +
                '}';
    }


    public String resumenBudget() {
        return "Movie {" +
                "id= " + id +
                ", title= '" + title + '\'' +
                ", release_date= '" + release_date + '\'' +
                ", budget= " + budget + "$" +
                '}';
    }

    public String resumenVoteAverage() {
        return "Movie {" +
                "id= " + id +
                ", title= '" + title + '\'' +
                ", release_date= '" + release_date + '\'' +
                ", rating= " + vote_average +
                '}';
    }


}
