import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class CounterThreads extends Thread {
    private final String notifiedOfOtherCompletion = "%s - Notified that other thread completed";
    private final String counting = "%s - Counting: %d";
    private final String completed = "%s - Completed counting.";
    private final int sleepMillis = 100;

    private final int startValue;
    private final int endValue;
    private final AtomicBoolean myCompletionFlag;
    private final AtomicBoolean otherCompletionFlag;
    private boolean otherThreadAcknowledged = false;
    static Logger logger = LogManager.getLogger(RunTwoCounterThreads.class);

    public CounterThreads(String name, int startValue, int endValue, AtomicBoolean myCompletionFlag, AtomicBoolean otherCompletionFlag) {
        super(name);
        this.startValue = startValue;
        this.endValue = endValue;
        this.myCompletionFlag = myCompletionFlag;
        this.otherCompletionFlag = otherCompletionFlag;
    }

    @Override
    public void run() {
        for (int i = startValue; i <= endValue; i++) {
            if (otherCompletionFlag.get() && !otherThreadAcknowledged) {
                logger.info(notifiedOfOtherCompletion.formatted(Thread.currentThread().getName()));
                otherThreadAcknowledged = true;
            }
            logger.info(counting.formatted(getName(), i));
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                logger.trace(e);
            }
        }
        myCompletionFlag.set(true);
        logger.info(completed.formatted(getName()));
    }
}
