package tracker.state;

import tracker.entity.Course;
import tracker.service.StatisticsService;
import tracker.service.StudentService;
import tracker.TrackerContext;

import java.util.Map;


public class FindState extends State {

    FindState(TrackerContext context) {
        super(context);
        addHandler("back", this::back);
        this.welcomeMessage = "Enter an id or 'back' to return:";
    }

    private void back() {
        context.setState(new MainState(context));
    }

    @Override
    void doDefault(String studentId) {
        final String NO_STUDENT_FOUND_TMPL = "No student is found for id=%s";

        if (!context.getStudentService().containsId(studentId)) {
            context.write(String.format(NO_STUDENT_FOUND_TMPL, studentId));
            return;
        }

        context.write(getLearningProgress(studentId));
    }

    private String getLearningProgress(String studentId) {
        StudentService studentService = context.getStudentService();
        StatisticsService statisticsService = context.getStatisticsService();
        Map<Course, Integer> coursesPoints = statisticsService.getCoursesPoints(studentService.getById(studentId));
        StringBuilder builder = new StringBuilder();
        builder.append(studentId).append(" points: ");

        StringBuilder results = new StringBuilder();
        for (Course course: Course.values()) {
            if (results.length() != 0) {
                results.append("; ");
            }
            results.append(course.name())
                    .append("=")
                    .append(coursesPoints.getOrDefault(course, 0));
        }
        return builder.append(results).toString();
    }
}
