package com.example.movies;

class Actor {
    private String name;
    private String characterName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    @Override
    public String toString() {
        return "Actor{name='" + name + "', characterName='" + characterName + "'}";
    }
}

class CrewMember {
    private String name;
    private String department;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "CrewMember{name='" + name + "', department='" + department + "', job='" + job + "'}";
    }
}
