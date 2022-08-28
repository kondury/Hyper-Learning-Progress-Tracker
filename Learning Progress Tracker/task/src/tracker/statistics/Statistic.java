package tracker.statistics;

import java.util.Comparator;

public enum Statistic {
    MOST_POPULAR(true),
    LEAST_POPULAR(false),
    HIGHEST_ACTIVITY(true),
    LOWEST_ACTIVITY(false),
    EASIEST_COMPLEXITY(true),
    HARDEST_COMPLEXITY(false);

    public final boolean reversedOrder;

    Statistic(boolean reversedOrder) {
        this.reversedOrder = reversedOrder;
    }
}
