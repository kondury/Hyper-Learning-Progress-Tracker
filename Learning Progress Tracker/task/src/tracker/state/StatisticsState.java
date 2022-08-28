package tracker.state;

import tracker.TrackerContext;
import tracker.entity.Course;
import tracker.service.StatisticsService;
import tracker.statistics.Statistic;
import tracker.statistics.StudentStatistics;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsState extends State {

    final String MOST_POPULAR_TMPL = "Most popular: %s\n"; // "n/a" or most students registered
    final String LEAST_POPULAR_TMPL = "Least popular: %s\n"; // "n/a" or least students registered
    final String HIGHEST_ACTIVITY_TMPL = "Highest activity: %s\n"; // "n/a" or bigger number of completed tasks
    final String LOWEST_ACTIVITY_TMPL = "Lowest activity: %s\n"; // "n/a" or lowest number of completed tasks
    final String EASIEST_COURSE_TMPL = "Easiest course: %s\n"; // "n/a" or highest average grade
    final String HARDEST_COURSE_TMPL = "Hardest course: %s\n"; // // "n/a" or lowest average grade

    public StatisticsState(TrackerContext trackerContext) {
        super(trackerContext);
        addHandler("back", this::back);
        this.welcomeMessage = "Type the name of a course to see details or 'back' to quit:\n" + prepareStatistics();
    }

    private void back() {
        context.setState(new MainState(context));
    }

    private String prepareStatistics() {
        StatisticsService service = context.getStatisticsService();
        
        var mostPopular = service.getCoursesByCategory(Statistic.MOST_POPULAR);
        var leastPopular = service.getCoursesByCategory(Statistic.LEAST_POPULAR);
        var highestActivity = service.getCoursesByCategory(Statistic.HIGHEST_ACTIVITY);
        var lowestActivity = service.getCoursesByCategory(Statistic.LOWEST_ACTIVITY);
        var hardest = service.getCoursesByCategory(Statistic.HARDEST_COMPLEXITY);
        var easiest = service.getCoursesByCategory(Statistic.EASIEST_COMPLEXITY);

        return String.format(MOST_POPULAR_TMPL, getCoursesAsString(mostPopular)) +
                String.format(LEAST_POPULAR_TMPL, getCoursesAsString(leastPopular)) +
                String.format(HIGHEST_ACTIVITY_TMPL, getCoursesAsString(highestActivity)) +
                String.format(LOWEST_ACTIVITY_TMPL, getCoursesAsString(lowestActivity)) +
                String.format(EASIEST_COURSE_TMPL, getCoursesAsString(easiest)) +
                String.format(HARDEST_COURSE_TMPL, getCoursesAsString(hardest));
    }

    private String getCoursesAsString(Collection<Course> list) {
        return Optional.ofNullable(list)
                .map(l -> l.stream()
                        .sorted()
                        .map(Enum::name)
                        .collect(Collectors.joining(", "))
                )
                .orElse("n/a");
    }

    private String getStudentStatsAsString(Collection<StudentStatistics> statistics) {
        return statistics.stream()
                .sorted(Comparator.comparingDouble(StudentStatistics::completionPercent)
                        .reversed()
                        .thenComparingInt(s -> Integer.parseInt(s.student().getId()))
                ).map(StudentStatistics::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    void doDefault(String input) {
        try {
            Course course = Course.getByName(input);
            String header = course.name() + "\nid\tpoints\tcompleted";
            context.write(header);
            var statistics = context.getStatisticsService().getStudentStatistics(course);
            String studentList = getStudentStatsAsString(statistics);
            if (!studentList.isEmpty()) {
                context.write(studentList);
            }
        } catch (IllegalArgumentException e) {
            context.write("Unknown course.");
        }
    }
}

