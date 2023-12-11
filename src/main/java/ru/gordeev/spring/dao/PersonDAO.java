package ru.gordeev.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.gordeev.spring.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static int PEOPLE_COUNT;

    public List<Person> index() {

        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class)); //BeanPropertyRowMapper заменяет стандартный маппер когда название параметров класса совпадают с названиями колонок
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id =?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name,age,email) VALUES (?,?,?)",person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatePerson) {
        jdbcTemplate.update("UPDATE Person SET name = ?,age = ?,email = ? WHERE id = ?", updatePerson.getName(), updatePerson.getAge(), updatePerson.getEmail(), id);

    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id = ?", id);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM Person");
    }

    //////////////////////////////////////////////
    //Тестим производительность пакетной вставки//
    //////////////////////////////////////////////
    public void testMultipleUpdate() {
        List<Person> people = create1000People();
        long before = System.currentTimeMillis();
        for (Person person: people
             ) {
            jdbcTemplate.update("INSERT INTO Person(name,age,email) VALUES (?,?,?)", person.getName(), person.getAge(), person.getEmail());
        }
        long after = System.currentTimeMillis();
        System.out.println("Время на вставку 1000 записей: "+(after-before));
    }
    public void testBachUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Person(name,age,email) VALUES (?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1,people.get(i).getName());
                preparedStatement.setInt(2,people.get(i).getAge());
                preparedStatement.setString(3,people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();
        System.out.println("Время на вставку 1000 записей: "+(after-before));
    }


    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            people.add(new Person(i,"person" + i, 30, "email" +i+ "@mail.ru"));
        }
        return people;
    }

}
