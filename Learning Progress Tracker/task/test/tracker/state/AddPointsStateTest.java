package tracker.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import tracker.entity.Student;


import static org.junit.jupiter.api.Assertions.*;

class AddPointsStateTest {

    StateTestUtils utils = new StateTestUtils();

    AddPointsState addPoints;

    @BeforeEach
    public void setUpStreams() {
        utils.setUpStreams();
        addPoints = new AddPointsState(utils.getContext());
        utils.getContext().setState(addPoints);
    }

    @AfterEach
    public void restoreStreams() {
        utils.restoreStreams();
        utils.getContext().close();
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {
            "id",
            "id 1",
            "id 1 2",
            "id 1 2 3",
            "id 1 2 3 4 5",
            "id -1 2 3 4",
            "id 1 -2 3 4",
            "id 1 2 -3 4",
            "id 1.1 2 3 4",
            "id 1 2 3 -4",
            "id nonNumber 2 3 4"
    })
    void doDefault_IncorrectInputFormat_IncorrectPointFormatMessage(String input) {
        //  1) empty input
        //  2) null input
        //  3) less then 5 tokens
        //  4) more then 5 tokens
        //  5) points tokens are nonNumbers
        //  6) points are not integer
        //  7) points tokens are negative
        Student student = new Student("id", "FirstName", "LastName", "name@domain.ru");
        utils.getContext().getStudentService().add(student);
        addPoints.doDefault(input);
        assertEquals("Incorrect points format.", utils.getOut());
    }

    @Test
    void doDefault_IdIsNotRegistered_NoStudentIsFoundMessage() {
        addPoints.doDefault("10000 1 2 3 4");
        assertEquals("No student is found for id=10000", utils.getOut());
    }

    @Test
    void doDefault_CorrectInputIdIsRegisteredWithoutPoints_PointsUpdatedMessage() {
        Student student = new Student("10000", "FirstName", "LastName", "name@domain.ru");
        utils.getContext().getStudentService().add(student);
        addPoints.doDefault("10000 1 2 3 4");
        assertEquals("Points updated.", utils.getOut());
    }

    @Test
    void doDefault_CorrectInputIdIsRegisteredWithPoints_PointsUpdatedMessage() {
        Student student = new Student("10000", "FirstName", "LastName", "name@domain.ru");
        utils.getContext().getStudentService().add(student);
        addPoints.doDefault("10000 1 2 3 4");
        assertEquals("Points updated.", utils.getOut());
    }


}