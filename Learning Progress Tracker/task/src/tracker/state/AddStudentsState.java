package tracker.state;

import tracker.entity.Student;
import tracker.entity.Person;
import tracker.TrackerContext;

import java.util.regex.Pattern;

public class AddStudentsState extends State {
    private int studentsAdded;

    private static final Pattern namePattern = Pattern.compile("(?:\\p{Alpha}+[-']?)+\\p{Alpha}+");
    private static final Pattern emailPattern =
            Pattern.compile("[^@]+@[\\p{Alnum}-]{1,61}(?:\\.\\p{Alnum}+)+");

    public AddStudentsState(TrackerContext context) {
        super(context);
        addHandler("back", this::back);
        this.welcomeMessage = "Enter student credentials or 'back' to return:";
    }

    @Override
    void doDefault(String input) {
        final String INCORRECT_CREDENTIALS = "Incorrect credentials."; // less then 3 different words
        final String INCORRECT_EMAIL = "Incorrect email."; // last word has format which is not similar to email format
        final String INCORRECT_FIRST_NAME = "Incorrect first name.";
        final String INCORRECT_LAST_NAME = "Incorrect last name.";
        final String STUDENT_ADDED = "The student has been added.";
        final String EMAIL_IS_ALREADY_TAKEN = "This email is already taken.";
        final String ERROR_DURING_ADDING = "Error: student was not added. Maybe id or email are already taken.";

        String[] data = input.split("\\s+");

        if (data.length < 3) {
            context.write(INCORRECT_CREDENTIALS);
            return;
        }

        String firstName = data[0];
        if (!isValidName(firstName)) {
            context.write(INCORRECT_FIRST_NAME);
            return;
        }

        String lastName = buildLastName(data, 1, data.length - 1);
        if (lastName == null) {
            context.write(INCORRECT_LAST_NAME);
            return;
        }

        String email = data[data.length - 1];
        if (!isValidEmail(email)) {
            context.write(INCORRECT_EMAIL);
            return;
        }

        Person person = context.getStudentService().getByEmail(email);
        if (person != null) {
            context.write(EMAIL_IS_ALREADY_TAKEN);
            return;
        }

        if (context.getStudentService().add(new Student(firstName, lastName, email))) {
            context.write(STUDENT_ADDED);
            studentsAdded++;
        } else {
            context.write(ERROR_DURING_ADDING);
        };
    }

    private String buildLastName(String[] data, int startIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            if (!isValidName(data[i])) {
                return null;
            }
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(data[i]);
        }
        return builder.toString();
    }

    private boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    private boolean isValidName(String name) {
        return namePattern.matcher(name).matches();
    }

    private void back() {
        String msg = "Total %d students have been added.";
        context.write(String.format(msg, studentsAdded));
        context.setState(new MainState(context));
    }
}
