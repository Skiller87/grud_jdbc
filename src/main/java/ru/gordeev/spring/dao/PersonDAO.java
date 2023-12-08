package ru.gordeev.spring.dao;

import org.springframework.stereotype.Component;
import ru.gordeev.spring.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;

    private static final String URL = "jdbc:postgresql:///first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "qwerty1234";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index (){
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return people;
    }

    public void save(Person person){
    //    person.setId(++PEOPLE_COUNT);
    //    people.add(person);
        try {
            Statement statement = connection.createStatement();
            String SQL = "INSERT INTO Person VALUES("+ 1 +",'" + person.getName() + "'," + person.getAge() +
            ",'" + person.getEmail() +"')";

            statement.executeUpdate(SQL);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(int id,Person updatePerson){
/*        Person personToBeUpdated = show (id);
        personToBeUpdated.setName(updatePerson.getName());
        //personToBeUpdated.setSurname(updatePerson.getSurname());
        personToBeUpdated.setAge(updatePerson.getAge());
        personToBeUpdated.setEmail(updatePerson.getEmail());*/
    }
    public void delete(int id){
        //people.removeIf(p -> p.getId() == id);
    }



    public Person show (int id) {

//        for (Person list: people
//             ) {
//            if (list.getId() == id) return list;
//        }
        return null;
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }
}
