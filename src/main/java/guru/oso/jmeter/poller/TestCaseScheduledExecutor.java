package guru.oso.jmeter.poller;

import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by BC on 12/25/16.
 */
public class TestCaseScheduledExecutor {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseScheduledExecutor.class);

    private ScheduledExecutorService scheduledExecutorService;

    private String messageNumber;
    private TestCaseTimestampDAO dao;


    public TestCaseScheduledExecutor(final String messageNumber, final TestCaseTimestampDAO dao) {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        this.dao = dao;
        this.messageNumber = messageNumber;

    }

    public TestCaseTimestamp pollForTestCase(final int delay) {

        TimestampCallable callable = new TimestampCallable(dao);

        ScheduledFuture<TestCaseTimestamp> scheduledFuture = scheduledExecutorService.schedule(callable, delay, TimeUnit.SECONDS);

        try {

            return scheduledFuture.get();

        } catch (InterruptedException ie) {
            logger.error("Something was interrupted.", ie);
            return null;
        } catch (ExecutionException ee) {
            logger.error("Something was executed.", ee);
            return null;
        } finally {
            scheduledExecutorService.shutdown();
        }

    }

    private class TimestampCallable implements Callable<TestCaseTimestamp> {

        TestCaseTimestampDAO store;

        public TimestampCallable(final TestCaseTimestampDAO dataStore) {

            this.store = dataStore;

        }

        public TestCaseTimestamp call() throws Exception {

            TestCaseTimestamp timestamp = this.store.findTestCase(messageNumber);
//            System.out.println("Executed!");

            logger.info("MessageNumber: " + timestamp.getMessageNumber());
            logger.info("MessageType: " + timestamp.getMessageType());
            logger.info("Start: " + timestamp.getStartTime() + " End: " + timestamp.getEndTime());

            return timestamp;

        }

    }

}
