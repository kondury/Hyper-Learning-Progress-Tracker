package tracker.entity;

public class Student extends Person {
    public Student(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
    }

    public Student(String id, String firstName, String lastName, String email) {
        super(id, firstName, lastName, email);
    }
}

