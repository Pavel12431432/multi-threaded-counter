import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class RunCounterThreads {
    public static void main(String[] args) {
        final String allThreadsCompleted = "All threads completed their execution.";

        Logger logger = LogManager.getLogger(RunCounterThreads.class);

        ArrayList<CounterThreads> threadList = new ArrayList<>(Arrays.asList(
                new CounterThreads("CounterThread-1", 1, 5),
                new CounterThreads("CounterThread-2", 100, 115),
                new CounterThreads("CounterThread-3", 10, 11)
        ));

        threadList.forEach(Thread::start);

        try {
            for (CounterThreads thread : threadList) {
                thread.join();
            }
        } catch (InterruptedException e) {
            logger.trace(e);
        }
        logger.info(allThreadsCompleted);
    }
}
