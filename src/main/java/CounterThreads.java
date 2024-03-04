import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CounterThreads extends Thread {
    // static map containing which threads have finished.
    private static final Map<String, Boolean> completionStatus = Collections.synchronizedMap(new HashMap<>());
    // internal map to keep track of which completions we have acknowledged in this thread.
    private final Map<String, Boolean> acknowledgedCompletions = new HashMap<>();
    private final String notifiedOfOtherCompletion = "%s - Notified that other thread (%s) completed";
    private final String counting = "%s - Counting: %d";
    private final String completed = "%s - Completed counting.";

    private final int startValue;
    private final int endValue;
    static Logger logger = LogManager.getLogger(RunCounterThreads.class);

    public CounterThreads(String name, int startValue, int endValue) {
        super(name);
        this.startValue = startValue;
        this.endValue = endValue;
        // state that this thread hasn't finished.
        completionStatus.put(name, false);
    }

    // check for any threads which have finished, but we haven't yet acknowledged
    private void checkOtherCompletion() {
        completionStatus.forEach((threadName, isCompleted) -> {
            if (!acknowledgedCompletions.containsKey(threadName)) {
                acknowledgedCompletions.put(threadName, false);
            }
            if (isCompleted && !acknowledgedCompletions.get(threadName)) {
                logger.info(notifiedOfOtherCompletion.formatted(getName(), threadName));
                acknowledgedCompletions.put(threadName, true); // Mark this completion as acknowledged
            }
        });
    }

    // mark this thread as completed in static map
    private void markCompletion() {
        completionStatus.put(getName(), true);
        logger.info(completed.formatted(getName()));
    }

    @Override
    public void run() {
        for (int i = startValue; i <= endValue; i++) {
            checkOtherCompletion();
            logger.info(counting.formatted(getName(), i));
        }
        markCompletion();
    }
}
