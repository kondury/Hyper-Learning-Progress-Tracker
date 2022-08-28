package tracker.state;

import tracker.TrackerContext;

import java.util.HashMap;
import java.util.Map;

public abstract class State {
    TrackerContext context;
    Map<String, Command> handlers;
    String welcomeMessage;

    State(TrackerContext context) {
        if (context == null) {
            throw new IllegalArgumentException("TrackerContext context cannot be null.");
        }
        this.context = context;
        handlers = new HashMap<>();
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void doAction(String input) {
        Command handler = handlers.get(input.trim());
        if (handler != null) {
            handler.execute();
        } else {
            doDefault(input);
        }
    }

    void addHandler(String name, Command command) {
        handlers.put(name, command);
    }

    abstract void doDefault(String input);
}
