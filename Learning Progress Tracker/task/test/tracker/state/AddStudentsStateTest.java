package tracker.state;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AddStudentsStateTest {

    StateTestUtils utils = new StateTestUtils();

    AddStudentsState addStudents;

    @BeforeEach
    public void setUpStreams() {
        utils.setUpStreams();
        addStudents = new AddStudentsState(utils.getContext());
        utils.getContext().setState(addStudents);
    }

    @AfterEach
    public void restoreStreams() {
        utils.restoreStreams();
        utils.getContext().close();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "help",
            "",
            "Firstname Secondname"
    })
    void doDefault_LessThen3WordsInput_IncorrectCredentials(String input) {
        addStudents.doDefault(input);
        assertEquals("Incorrect credentials.", utils.getOut());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a Secondname whop@domain.net",
            "A. Secondname whop@domain.net",
            "'a Secondname whop@domain.net",
            "-a Secondname whop@domain.net",
            "z-'a Secondname whop@domain.net",
            "z- Secondname whop@domain.net",
            "Z' Secondname whop@domain.net",

    })
    void doDefault_WrongFirstNameTest_IncorrectFirstName(String input) {
        addStudents.doDefault(input);
        assertEquals("Incorrect first name.", utils.getOut());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Firstname a Secondname whop@domain.net",
            "Firstname A. Secondname whop@domain.net",
            "Firstname 'a Secondname whop@domain.net",
            "Firstname -a Secondname whop@domain.net",
            "Firstname z-'a Secondname whop@domain.net",
            "Firstname z- Secondname whop@domain.net",
            "Firstname Z' Secondname whop@domain.net",

    })
    void doDefault_WrongLastNameTest_IncorrectFirstName(String input) {
        addStudents.doDefault(input);
        assertEquals("Incorrect last name.", utils.getOut());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Firstname Secondname whop@@domain.net",
            "Firstname Secondname @domain.net",
            "Firstname Secondname name@domain",
            "Firstname Secondname name.com",
    })
    void doDefault_EmailTest_IncorrectEmail(String input) {
        addStudents.doDefault(input);
        assertEquals("Incorrect email.", utils.getOut());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Firstname Secondname name@domain.com",
            "F'irstname Secondname name@domain.com",
            "Fir's't-n-ame Secondname name@domain.com",
            "Firstname More Then One Word Secondname name@domain.com",
            "Firstname Secondname n12me@domain.com.org",
            "Mary Emelianenko 125367at@zzz90.z9",
            "Ed Eden a1@a1.a1"
    })
    void doDefault_CorrectCredentialsTest_StudentHaveBeenAdded(String input) {
        addStudents.doDefault(input);
        assertEquals("The student has been added.", utils.getOut());
    }

    @Test
    void doDefault_DuplicatedEmail_Email_is_already_taken() {
        String firstTimeInput = "Firstname Secondname name@domain.com";
        String sameEmailButDifferentNames = "AnotherName AnotherName name@domain.com";
        addStudents.doDefault(firstTimeInput);
        assertEquals("The student has been added.", utils.getOut());
        utils.clear();
        addStudents.doDefault(sameEmailButDifferentNames);
        assertEquals("This email is already taken.", utils.getOut());
        utils.clear();
        addStudents.doDefault(sameEmailButDifferentNames);
        assertEquals("This email is already taken.", utils.getOut());
    }

}