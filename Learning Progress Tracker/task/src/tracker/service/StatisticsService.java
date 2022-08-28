package tracker.service;

import tracker.TrackerContext;
import tracker.entity.Course;
import tracker.entity.Enrollment;
import tracker.entity.Student;
import tracker.entity.Submission;
import tracker.statistics.CourseStatistics;
import tracker.statistics.Statistic;
import tracker.statistics.StudentStatistics;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class StatisticsService {

    private final StudentService studentService;

    private Collection<CourseStatistics> courseStatistics;

    public StatisticsService(TrackerContext trackerContext) {
        studentService = trackerContext.getStudentService();
    }

    public Collection<Course> getCoursesByCategory(Statistic category) {
        updateStatistics();

        ToDoubleFunction<CourseStatistics> measurer = getMeasurer(category);
        var orderedStats = courseStatistics.stream()
                .collect(
                        Collectors.groupingBy(
                                measurer::applyAsDouble,
                                TreeMap::new,
                                Collectors.toList()
                        )
                );

        // everytime take off the list having max value in category
        // and only later take min value list to ensure it will be empty
        // if the scale processed has a single value
        var stats = Optional.ofNullable(orderedStats.pollLastEntry());
        if (!category.reversedOrder) {
                stats = Optional.ofNullable(orderedStats.pollFirstEntry());
        }

        return stats.map(
                entry -> entry.getValue().stream()
                        .map(CourseStatistics::course)
                        .collect(Collectors.toList())
                )
                .orElse(null);
    }

    private ToDoubleFunction<CourseStatistics> getMeasurer(Statistic category) {
        return switch (category) {
            case MOST_POPULAR, LEAST_POPULAR -> CourseStatistics::participants;
            case HIGHEST_ACTIVITY, LOWEST_ACTIVITY -> CourseStatistics::completedTasks;
            case EASIEST_COMPLEXITY, HARDEST_COMPLEXITY -> stat -> (double) stat.totalPoints() / stat.completedTasks();
        };
    }

    private void updateStatistics() {
        courseStatistics = getStatistics();
    }

    public Map<Course, Integer> getCoursesPoints(Student student) {
        return studentService.getSubmissions().stream()
                .filter(e -> e.student().equals(student))
                .collect(Collectors.groupingBy(
                        Submission::course,
                        Collectors.summingInt(Submission::points)
                ));
    }

    private Collection<CourseStatistics> getStatistics() {
        return studentService.getSubmissions().stream().collect(
                Collectors.groupingBy(
                        Submission::course,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            long completedTasks = list.size();
                            long totalPoints = list.stream().mapToInt(Submission::points).sum();
                            long participants = list.stream().map(Submission::student).distinct().count();
                            Course course = list.get(0).course();
                            return new CourseStatistics(course, participants, completedTasks, totalPoints);
                        })
                )).values();
    }

    public Collection<StudentStatistics> getStudentStatistics(Course course) {
        return studentService.getSubmissions().stream()
                .filter(e -> e.course() == course)
                .collect(Collectors.groupingBy(
                        Submission::student,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            long completedTasks = list.size();
                            long totalPoints = list.stream().mapToInt(Submission::points).sum();
                            Student student = list.get(0).student();
                            return new StudentStatistics(student, course, completedTasks, totalPoints);
                        })
                )).values().stream()
                .filter(e -> e.totalPoints() > 0)
                .collect(Collectors.toList());
    }

    public Collection<StudentStatistics> getStudentStatistics() {
        return studentService.getSubmissions().stream()
                .collect(Collectors.groupingBy(
                        s -> new Enrollment(s.student(), s.course()),
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            long completedTasks = list.size();
                            long totalPoints = list.stream().mapToInt(Submission::points).sum();
                            Student student = list.get(0).student();
                            Course course = list.get(0).course();
                            return new StudentStatistics(student, course, completedTasks, totalPoints);
                        })
                )).values().stream()
//                .filter(e -> e.totalPoints() > 0)
                .collect(Collectors.toList());
    }

}
