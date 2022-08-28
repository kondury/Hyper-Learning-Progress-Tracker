package tracker.state;

import tracker.*;
import tracker.entity.Course;
import tracker.entity.Student;
import tracker.entity.Submission;
import tracker.entity.SubmissionFactory;
import tracker.service.StudentService;

import java.util.*;
import java.util.stream.Collectors;

public class AddPointsState extends State {

    AddPointsState(TrackerContext context) {
        super(context);
        addHandler("back", this::back);
        this.welcomeMessage = "Enter an id and points or 'back' to return:";
    }

    private void back() {
        context.setState(new MainState(context));
    }

    @Override
    void doDefault(String input) {
        final String INCORRECT_POINTS_FORMAT = "Incorrect points format.";
        final String NO_STUDENT_FOUND_TMPL = "No student is found for id=%s";
        final String POINTS_UPDATED = "Points updated.";

        if (input == null || input.isEmpty()) {
            context.write(INCORRECT_POINTS_FORMAT);
            return;
        }

        String[] data = input.split("\\s+");

        if (data.length != 5) {
            context.write(INCORRECT_POINTS_FORMAT);
            return;
        }

        Student student = context.getStudentService().getById(data[0]);
        if (student == null) {
            context.write(String.format(NO_STUDENT_FOUND_TMPL, data[0]));
            return;
        }

        List<Submission> submissions = parsePoints(student, data);
        if (submissions.isEmpty()) {
            context.write(INCORRECT_POINTS_FORMAT);
            return;
        }

        try {
            StudentService service = context.getStudentService();
            service.submit(submissions);
            context.write(POINTS_UPDATED);
        } catch (Exception e) {
            context.write(e.getMessage());
            context.write(Arrays.toString(e.getStackTrace()));
        }
    }

    private List<Submission> parsePoints(Student student, String[] data) {
        try {
            return Arrays.stream(Course.values())
                    .map(course -> SubmissionFactory.createSubmission(student, course.id, data[course.id]))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
