import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunCounterThreads {
    public static void main(String[] args) {
        final String allThreadsCompleted = "All threads completed their execution.";
        final String thread1Name = "CounterThread-1";
        final String thread2Name = "CounterThread-2";
        final String thread3Name = "CounterThread-3";

        Logger logger = LogManager.getLogger(RunCounterThreads.class);

        CounterThreads thread1 = new CounterThreads(thread1Name, 1, 3);
        CounterThreads thread2 = new CounterThreads(thread2Name, 10, 15);
        CounterThreads thread3 = new CounterThreads(thread3Name, 100, 110);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            logger.trace(e);
        }
        logger.info(allThreadsCompleted);
    }
}
