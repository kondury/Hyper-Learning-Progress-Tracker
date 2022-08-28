package tracker.service;

import tracker.entity.Person;

import java.util.*;

public class PersonService {

    private static int lastId = 9999;
    Map<String, Person> personsById;
    Map<String, Person> personsByEmail;

    public PersonService() {
        this.personsByEmail = new HashMap<>();
        this.personsById = new LinkedHashMap<>();
    }

    public boolean add(Person person) {
        if (!person.hasId()) {
            person.setId(generateId());
        }

        if (personsById.containsKey(person.getId())) {
            return false;
        }

        if (personsByEmail.containsKey(person.getEmail())) {
            return false;
        }

        personsById.put(person.getId(), person);
        personsByEmail.put(person.getEmail(), person);
        return true;
    }

    private String generateId() {
        lastId++;
        return String.format("%05d", lastId);
    }

    public boolean containsId(String id) {
        return personsById.containsKey(id);
    }

    public Set<String> getRegisteredIds() {
        return personsById.keySet();
    }

    public Person getByEmail(String email) {
        return personsByEmail.get(email);
    }

    public Person getById(String id) {
        return personsById.get(id);
    }

}
