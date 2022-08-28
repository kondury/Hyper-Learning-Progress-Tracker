package tracker.state;

import tracker.TrackerContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StateTestUtils {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    TrackerContext context = new TrackerContext();

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public TrackerContext getContext() {
        return context;
    }

    public String getOut() {
        return outContent.toString().trim();
    }

    public void clear() {
        outContent.reset();
    }



}
