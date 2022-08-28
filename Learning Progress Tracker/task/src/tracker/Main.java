package tracker;

public class Main {

    public static void main(String[] args) {
        TrackerContext context = new TrackerContext();
        Tracker tracker = new Tracker(context);
        tracker.run();
    }
}
