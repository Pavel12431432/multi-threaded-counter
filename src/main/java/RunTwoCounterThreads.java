import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class RunTwoCounterThreads {
    public static void main(String[] args) {
        final String bothThreadsCompleted = "Both threads completed their execution.";
        final String thread1Name = "CounterThread-1";
        final String thread2Name = "CounterThread-2";

        Logger logger = LogManager.getLogger(RunTwoCounterThreads.class);

        AtomicBoolean completionFlag1 = new AtomicBoolean(false);
        AtomicBoolean completionFlag2 = new AtomicBoolean(false);

        CounterThreads thread1 = new CounterThreads(thread1Name, 1, 5, completionFlag1, completionFlag2);
        CounterThreads thread2 = new CounterThreads(thread2Name, 6, 15, completionFlag2, completionFlag1);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.trace(e);
        }

        logger.info(bothThreadsCompleted);
    }
}