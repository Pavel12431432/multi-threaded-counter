import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CounterThreads extends Thread {
    private static final Map<String, Boolean> completionStatus = Collections.synchronizedMap(new HashMap<>());
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
        completionStatus.put(name, false);
        completionStatus.keySet().forEach(key -> acknowledgedCompletions.put(key, false));
    }

    @Override
    public void run() {
        for (int i = startValue; i <= endValue; i++) {
            // Check other threads' completion status
            completionStatus.forEach((threadName, isCompleted) -> {
                if (isCompleted && !acknowledgedCompletions.get(threadName)) {
                    logger.info(notifiedOfOtherCompletion.formatted(getName(), threadName));
                    acknowledgedCompletions.put(threadName, true); // Mark this completion as acknowledged
                }
            });

            logger.info(counting.formatted(getName(), i));
        }

        // Mark this thread as completed
        completionStatus.put(getName(), true);
        logger.info(completed.formatted(getName()));
    }
}
