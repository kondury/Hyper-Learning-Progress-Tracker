package tracker.statistics;

import tracker.entity.Course;
import tracker.entity.Student;

import java.util.Comparator;

public record StudentStatistics(Student student, Course course, long completedTasks, long totalPoints) {
    public static double completionPercent(StudentStatistics stat) {
        return 100D * stat.totalPoints() / stat.course.completionPoints;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%01.01f%%",student.getId(), totalPoints, completionPercent(this));
    }

    public static Comparator<StudentStatistics> studentStatisticsComparator() {
        return Comparator.comparingDouble(StudentStatistics::completionPercent)
                .reversed()
                .thenComparingInt(s -> Integer.parseInt(s.student().getId()));
    }
}
