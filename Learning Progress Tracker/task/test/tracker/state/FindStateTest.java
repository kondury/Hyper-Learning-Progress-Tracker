package tracker.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import tracker.entity.Course;
import tracker.entity.Student;
import tracker.entity.Submission;

import static org.junit.jupiter.api.Assertions.*;

class FindStateTest {
    StateTestUtils utils = new StateTestUtils();

    FindState findState;

    @BeforeEach
    public void setUpStreams() {
        utils.setUpStreams();
        findState = new FindState(utils.getContext());
        utils.getContext().setState(findState);
    }

    @AfterEach
    public void restoreStreams() {
        utils.restoreStreams();
        utils.getContext().close();
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = { "10000" })
    void doDefault_UnknownStudentIdOrEmptyOrNull_NoStudentFoundMessage(String input) {
        findState.doDefault(input);
        assertEquals("No student is found for id=" + input, utils.getOut());
    }

    @Test
    void doDefault_ExistingStudentIdEntered_LearningProgressPrinted() {
        var service = utils.getContext().getStudentService();
        Student student = new Student("10000", "FirstName", "LastName", "name@domain.ru");

        // adding some points before adding to service
        service.submit(new Submission(student, Course.Databases, 20));
        service.submit(new Submission(student, Course.Java, 5));
        service.submit(new Submission(student, Course.Spring, 100));

        utils.getContext().getStudentService().add(student);

        // adding some points after adding to service
        service.submit(new Submission(student, Course.DSA, 10));
        service.submit(new Submission(student, Course.Spring, 200));

        findState.doDefault("10000");

        assertEquals("10000 points: Java=5; DSA=10; Databases=20; Spring=300", utils.getOut());
    }

}