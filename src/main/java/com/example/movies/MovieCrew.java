package com.example.movies;

public class MovieCrew {
    private String person_name;
    private String job;
    private String department_name;

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
    @Override
    public String toString() {
        return "Employed{name=" + person_name + ", Department='" + department_name + " Job='" + job + "'}";
    }
}
