package tracker.entity;

public class SubmissionFactory {
    public static Submission createSubmission(Student student, int courseId, String pointsParam) {
        Course course = Course.getById(courseId);
        int points = Integer.parseInt(pointsParam);
        if (points < 0) {
            throw new IllegalArgumentException("Negative point value is impossible.");
        }
        return new Submission(student, course, points);
    }
}