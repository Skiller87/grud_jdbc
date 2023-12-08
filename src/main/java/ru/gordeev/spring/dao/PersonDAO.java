package ru.gordeev.spring.dao;

import org.springframework.stereotype.Component;
import ru.gordeev.spring.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private List<Person> people;
    private static int PEOPLE_COUNT;

    {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT,"Tom", 32,"tom@tom.ru"));
        people.add(new Person(++PEOPLE_COUNT,"Scotty", 18, "scotty@mail.ru"));
        people.add(new Person(++PEOPLE_COUNT,"Bob",23,"bobik@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT,"Lola",27, "lolitochka@mail.ru"));
    }

    public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }
    public void update(int id,Person updatePerson){
        Person personToBeUpdated = show (id);
        personToBeUpdated.setName(updatePerson.getName());
        //personToBeUpdated.setSurname(updatePerson.getSurname());
        personToBeUpdated.setAge(updatePerson.getAge());
        personToBeUpdated.setEmail(updatePerson.getEmail());
    }
    public void delete(int id){
        people.removeIf(p -> p.getId() == id);
    }

    public List<Person> index (){
        return people;
    }

    public Person show (int id) {

//        for (Person list: people
//             ) {
//            if (list.getId() == id) return list;
//        }
//        return null;
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }
}
