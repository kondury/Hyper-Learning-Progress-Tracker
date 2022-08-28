package tracker.state;

import tracker.TrackerContext;
import tracker.entity.Enrollment;
import tracker.service.NotificationService;

import java.util.function.Function;

public class MainState extends State {

    public MainState(TrackerContext context) {
        super(context);
        addHandler("exit", this::exit);
        addHandler("add students", () -> changeState(AddStudentsState::new));
        addHandler("back", this::back);
        addHandler("", this::noInput);
        addHandler("list", this::list);
        addHandler("add points", () -> changeState(AddPointsState::new));
        addHandler("find", () -> changeState(FindState::new));
        addHandler("statistics", () -> changeState(StatisticsState::new));
        addHandler("notify", this::notifyCompletion);

        this.welcomeMessage = "Learning Progress Tracker";
    }

    @Override
    void doDefault(String input) {
        context.write("Unknown command!");
    }

    private void back() {
        context.write("Enter 'exit' to exit the program.");
    }

    private void noInput() {
        context.write("No input.");
    }

    private void exit() {
        context.write("Bye!");
        context.setState(null);
    }

    private void list() {
        var ids = context.getStudentService().getRegisteredIds();
        if (ids.isEmpty()) {
            context.write("No students found.");
        } else {
            context.write("Students:");
            ids.forEach(System.out::println);
        }
    }

    private void notifyCompletion() {
        var toNotify = context.getNotificationService().getNotificationList();
        toNotify.forEach(this::notifyCompletion);
        long totalNotified = toNotify.stream().map(Enrollment::student).distinct().count();
        context.write("Total %d students have been notified.".formatted(totalNotified));
        context.getNotificationService().updateNotified(toNotify);
    }

    private void notifyCompletion(Enrollment e) {
        context.write("To: " + e.student().getEmail());
        context.write("Re: Your Learning Progress");
        context.write("Hello, %s %s! You have accomplished our %s course!"
                .formatted(e.student().getFirstName(), e.student().getLastName(), e.course())
        );
    }

    private void changeState(Function<TrackerContext, State> stateConstructor) {
        State state = stateConstructor.apply(context);
        context.write(state.getWelcomeMessage());
        context.setState(state);
    }
}
