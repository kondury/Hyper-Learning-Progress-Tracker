package tracker.statistics;

import tracker.entity.Course;

public record CourseStatistics(Course course, long participants, long completedTasks, long totalPoints) {

//    public static int comparePopularity(CourseStatistics x, CourseStatistics y) {
//        return Long.compare(x.participants, y.participants);
//    }
//
//    public static int compareActivity(CourseStatistics x, CourseStatistics y) {
//        return Long.compare(x.completedTasks, y.completedTasks);
//    }
//
//    public static int compareEasiness(CourseStatistics x, CourseStatistics y) {
//        double xAvg = (double) x.totalPoints / x.completedTasks;
//        double yAvg = (double) y.totalPoints / y.completedTasks;
//        return Double.compare(xAvg, yAvg);
//    }

    public static double averagePoints(CourseStatistics stat) {
        return (double) stat.totalPoints() / stat.completedTasks();
    }

}
