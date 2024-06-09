package com.example.movies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PersonDAO {
    private Connection connection;

    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM person WHERE person_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Person> findById(int id) {
        String sql = "SELECT * FROM person WHERE person_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Person person = new Person();
                    person.setId(resultSet.getInt("person_id"));
                    person.setName(resultSet.getString("person_name"));
                    return Optional.of(person);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Person save(Person person) {
        String sql = person.getId() == null
                ? "INSERT INTO person (person_name) VALUES (?)"
                : "UPDATE person SET person_name = ? WHERE person_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.getName());
            if (person.getId() == null) {
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        person.setId(generatedKeys.getInt(1));
                    }
                }
            } else {
                statement.setInt(2, person.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }
}
