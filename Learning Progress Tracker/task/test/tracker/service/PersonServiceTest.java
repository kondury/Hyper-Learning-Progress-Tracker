package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tracker.entity.Person;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    PersonService service;

    @BeforeEach
    void preparePersonService() {
        service = new PersonService();
    }

    @ParameterizedTest
    @CsvSource({
            "John Doe john@doe.com, 10000, true", // common case, id stored
            "John Doe john@doe.com, UNKNOWN_ID, false", // unknown id, id assigned
            "John Doe john@doe.com, '', false" // empty id, id assigned
    })
    void add_addPerson_SingleRecordStored(String personAttrs, String id, boolean check4Result) {
        String[] data = personAttrs.split(" ");
        if (id.equals("UNKNOWN_ID")) {
            id = Person.UNKNOWN_ID;
        }
        Person person = new Person(id, data[0], data[1], data[2]);

        assertEquals(0, service.getRegisteredIds().size());
        assertTrue(service.add(person));
        assertEquals(1, service.getRegisteredIds().size());
        assertEquals(check4Result, service.containsId(id));
    }

    @ParameterizedTest
    @CsvSource({
            "John, Doe, duplicated@email.com, ORIGINAL_ID, Eduard, Snow, duplicated@email.com, ANOTHER_ID",
            "John, Doe, original@email.com, DUPLICATED_ID, Eduard, Snow, another@email.com, DUPLICATED_ID"
    })
    void add_addPersonWithDuplicateEmailOrId_DuplicateDoesNotAddOrChangeStoredEarlier(
            String firstName1, String lastName1, String email1, String id1,
            String firstName2, String lastName2, String email2, String id2) {

        assertEquals(0, service.getRegisteredIds().size());

        Person person = new Person(id1, firstName1, lastName1, email1);
        assertTrue(service.add(person));

        assertEquals(1, service.getRegisteredIds().size());
        person = new Person(id2, firstName2, lastName2, email2);

        assertFalse(service.add(person));
        assertEquals(1, service.getRegisteredIds().size());

        Person fromService = service.getByEmail(email1);
        assertEquals(firstName1, fromService.getFirstName());
        assertEquals(lastName1, fromService.getLastName());

        fromService = service.getById(id1);
        assertEquals(firstName1, fromService.getFirstName());
        assertEquals(lastName1, fromService.getLastName());
    }
}