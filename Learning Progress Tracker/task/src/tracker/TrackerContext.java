package tracker;

import tracker.service.NotificationService;
import tracker.service.StatisticsService;
import tracker.service.StudentService;
import tracker.state.MainState;
import tracker.state.State;

import java.util.Scanner;

public class TrackerContext {
    private final StudentService studentService;
    private final StatisticsService statisticsService;
    private final NotificationService notificationService;
    private final Scanner scanner = new Scanner(System.in);

    private State state;

    public TrackerContext() {
        this.state = new MainState(this);
        this.studentService = new StudentService();
        this.statisticsService = new StatisticsService(this);
        this.notificationService = new NotificationService(this);
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    String read() {
        return scanner.nextLine();
    }

    public void write(String msg) {
        System.out.println(msg);
    }

    public void close() {
        scanner.close();
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
