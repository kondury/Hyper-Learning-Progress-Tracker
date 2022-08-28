package tracker.service;

import tracker.entity.Course;
import tracker.entity.Student;
import tracker.entity.Submission;
import tracker.statistics.CourseStatistics;
import tracker.statistics.StudentStatistics;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService extends PersonService {

    List<Submission> submissions;

    public StudentService() {
        super();
        submissions = new ArrayList<>();
    }

    // // TODO: 8/24/2022 refactor into packages by feature design 
    // todo 1) isolate stats generation (from submission history)
    //      2) generate stats by each course (courses can be sorted by criteria
    //      3) generate stats for each asked <course, student>
    //          how stats can be calculated in an lazy manner
    //          (is it possible, or calculation always would be different)
    //          pairs
    //              most-least popular - total enrolled students by course
    //              highest-lowest activity - total unique submissions by course
    //              easiest-hardest course average points = (total points of all students / total students) by course
    //              could be calculated simultaneously
    //
    // todo
    //  1) where to calculate stats,
    //  2) who have to define how it will look like

    // todo notification queue
    //      0) Tests for Statistics and notifcation services
    // todo 1) notification queue
    //          calculate all notification is a bad idea, so they have to be accumulated during the study.
    //          so there to be a trigger which will refresh statistics in a period of time.
    //          When new submissions appear (statistics queried) there need to distinguish:
    //                  1. Completed courses.
    //                      earlier completed do not create new notifications when course was completed earlier
    //                      newly completed - create notification
    //                      Course completion history - can be the same as Notification queue.
    //      2) Marker that new submissions have appeared.
    //      3) Marker remembers position from where to continue statistics calculation
    //      4) Statistics can be updated when
    //          - "statistics" command is called
    //          - "notify" command is called
    //          variant 1: full update of statistics
    //          variant 2: incremental update of statistics
    //      5) StatisticsService - separate from StudentService
    //          updateStatistics()
    //              - general statistics
    //              - StudentByCourse statistics
    //          getListOfStudentsByCourse
    //          getGeneralStats by chosen scale
    //          ask from StudentService latest submissions submitted after Marker
    //          getCompletions(TimeMarker)
    //      6) NotificationService - separate from Statistics and Student services
    //          ask new completions from StatisticsService() which have been occurred after NotificationMarker
    //      7) StudentService - return latest submissions after StatisticsMarker
    //

    @Override
    public Student getById(String id) {
        return (Student) super.getById(id);
    }

    @Override
    public Student getByEmail(String email) {
        return (Student) super.getByEmail(email);
    }

    public void submit(Submission submission) {
        submissions.add(submission);
    }

    public void submit(List<Submission> submissions) {
        this.submissions.addAll(submissions);
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }
}
