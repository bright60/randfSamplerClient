package guru.oso.jmeter.poller;

import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.mongo.TestCaseDataStore;
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

    public TestCaseScheduledExecutor(final String messageNumber) {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        this.messageNumber = messageNumber;

    }

    public TestCaseTimestamp pollForTestCase(final int delay) {

        TimestampCallable callable = new TimestampCallable();

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

        TestCaseDataStore store;

        public TimestampCallable() {

            this.store = new TestCaseDataStore("mule_user", "mule_user", "localhost", "mule_perf_test");

        }

        public TestCaseTimestamp call() throws Exception {

            TestCaseTimestamp timestamp = this.store.findTestCase(messageNumber);
//            System.out.println("Executed!");

            return timestamp;

        }

    }

}
