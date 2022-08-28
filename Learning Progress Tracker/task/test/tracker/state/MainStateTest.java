package tracker.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import tracker.entity.Course;
import tracker.entity.Student;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MainStateTest {

    StateTestUtils utils = new StateTestUtils();

    MainState mainState;



    @BeforeEach
    public void setUpStreams() {
        utils.setUpStreams();
        mainState = new MainState(utils.getContext());
        utils.getContext().setState(mainState);
    }

    @AfterEach
    public void restoreStreams() {
        utils.restoreStreams();
        utils.getContext().close();
    }

    @ParameterizedTest
    @CsvSource({
            "exit, Bye!",
            "back, Enter \'exit\' to exit the program.",
            "'', No input.",
            "askdfj, Unknown command!"

    })
    void doAction(String input, String output) {
        mainState.doAction(input);
        assertEquals(output, utils.getOut());
    }

    @ParameterizedTest
    @CsvSource({
        "add students, tracker.state.AddStudentsState",
        "add points, tracker.state.AddPointsState",
        "find, tracker.state.FindState"
    })
    void doAction_ChangingStateCommand_StateChangedAndWelcomeMessagePrinted(String command, String className) {
        try {
            mainState.doAction(command);
            Class<?> clazz = Class.forName(className);
            assertEquals(clazz, utils.getContext().getState().getClass());
            assertEquals(utils.context.getState().getWelcomeMessage(), utils.getOut());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void doAction_listCommandWhenNoStudents_NoStudentsFoundMessagePrinted() {
        mainState.doAction("list");
        assertEquals("No students found.", utils.getOut());
    }

    @ParameterizedTest
    @MethodSource("getStudentsSource")
    void doAction_listCommandWhenStudentsAdded_NoStudentsFoundMessagePrinted(String idList, List<String> students) {

        for (var input: students) {
            String[] data = input.split(" ");
            Student student = new Student(data[0], data[1], data[2], data[3]);
            utils.getContext().getStudentService().add(student);
        }
        mainState.doAction("list");
        assertEquals("Students:" + idList, utils.getOut().replaceAll("\\s+", "|"));
    }

    public static Stream<Arguments> getStudentsSource() {
        return Stream.of(
            arguments("|10000", List.of("10000 John Doe john@doe.com")),
            arguments(("|10000|10001"), List.of("10000 John Doe john@doe.com", "10001 Jane Doe jane@doe.com"))
        );
    }
}