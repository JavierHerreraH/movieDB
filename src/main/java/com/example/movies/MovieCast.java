package com.example.movies;

public class MovieCast {
    //private Person person;
    private String name;
    private String character_name;

   /* public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }*/

    public String getCharacter_name() {
        return character_name;
    }

    public void setCharacter_name(String character_name) {
        this.character_name = character_name;
    }

    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Actor{name=" + name + ", character='" + character_name + "'}";
    }
}

