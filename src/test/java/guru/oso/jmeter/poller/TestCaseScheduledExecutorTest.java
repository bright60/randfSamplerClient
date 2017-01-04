package guru.oso.jmeter.poller;

import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.mongo.TestCaseDataStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by BC on 12/26/16.
 */
public class TestCaseScheduledExecutorTest {

    private TestCaseScheduledExecutor executor;
    private TestCaseDataStore store;
    private TestCaseTimestamp timestamp;

    @Before
    public void setUp() throws Exception {

        timestamp = this.createTimestamp("0000000000000001",1482533994L);
        this.store = new TestCaseDataStore("mule_user", "mule_user", "localhost", "mule_perf_test");
        this.executor = new TestCaseScheduledExecutor("0000000000000001");

    }

    @After
    public void tearDown() throws Exception {

        this.executor = null;
        this.store = null;
        this.timestamp = null;

    }

    @Test
    public void schedule() throws Exception {

//        this.store.insertTestCase(timestamp);

        TestCaseTimestamp timestamp = this.executor.pollForTestCase(10);
        if (timestamp instanceof RealTestCaseTimestamp) {
            System.out.println("Real Test Case");
            assertEquals("0000000000000001", timestamp.getMessageNumber());
            assertEquals(Long.valueOf(1482533994L), timestamp.getTimestamp());
        } else if (timestamp instanceof NullTestCaseTimeStamp){
            System.out.println("Null Test Case");
            assertEquals(TestCaseTimestamp.NULL_MESSAGE_NUMBER, timestamp.getMessageNumber());
            assertEquals(TestCaseTimestamp.NULL_TIMESTAMP, timestamp.getTimestamp());
        }

        this.store.dropAllTestCases();

    }

    private TestCaseTimestamp createTimestamp(final String mNumber, final Long timestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setTimestamp(timestamp);

        return tct;

    }

}