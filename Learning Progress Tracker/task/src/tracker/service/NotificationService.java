package tracker.service;

import tracker.TrackerContext;
import tracker.entity.Course;
import tracker.entity.Enrollment;
import tracker.entity.Student;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationService {

    private final StatisticsService statisticsService;
    Set<Enrollment> notifiedOfCompletion = new HashSet<>();

    public NotificationService(TrackerContext trackerContext) {
        this.statisticsService = trackerContext.getStatisticsService();
    }

    public Collection<Enrollment> getNotificationList() {
        var completionsAll = updateCompletions();
        return completionsAll.stream()
                .filter(e -> !notifiedOfCompletion.contains(e))
                .collect(Collectors.toList());
    }

    public void updateNotified(Collection<Enrollment> completions) {
        notifiedOfCompletion.addAll(completions);
    }

    private Collection<Enrollment> updateCompletions() {
        return statisticsService.getStudentStatistics().stream()
                .filter(s -> s.totalPoints() >= s.course().completionPoints)
                .map(s -> new Enrollment(s.student(), s.course()))
                .collect(Collectors.toList());
    }
}
