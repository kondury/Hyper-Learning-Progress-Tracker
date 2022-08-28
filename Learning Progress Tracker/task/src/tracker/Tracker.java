package tracker;

public class Tracker {
    private final TrackerContext context;

    public Tracker(TrackerContext context) {
        if (context == null) {
            throw new IllegalArgumentException("TrackerContext context parameter cannot be null");
        }
        this.context = context;
    }

    void run() {
        context.write(context.getState().getWelcomeMessage());
        while (context.getState() != null) {
            context.getState().doAction(context.read());
        }
        context.close();
    }
}
