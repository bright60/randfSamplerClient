package guru.oso.jmeter.mongo;

import com.mongodb.BasicDBObjectBuilder;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BC on 12/25/16.
 */
public class TestCaseDataStoreTest {

    public static final long DEFAULT_TIME = 1483115162199L;

    private TestCaseDataStore store;

    @Before
    public void setUp() throws Exception {

        this.store = new TestCaseDataStore("mule_user", "mule_user", "localhost", "mule_perf_test");

    }

    @After
    public void tearDown() throws Exception {

        this.store.dropAllTestCases();

        this.store = null;

    }

    @Test
    public void getAllTestCases() throws Exception {

        TestCaseTimestamp testCaseOne = this.createTimestamp("0000000000000001",DEFAULT_TIME);
        TestCaseTimestamp testCaseTwo = this.createTimestamp("0000000000000002",DEFAULT_TIME);

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        List<TestCaseTimestamp> timestamps = this.store.getAllTestCases();
        assertEquals(2, timestamps.size());
        for (TestCaseTimestamp timestamp : timestamps) {

            System.out.println("MessageNumber:" + timestamp.getMessageNumber() + " Timestamp:" + timestamp.getTimestamp());

        }

    }

    @Test
    public void insertTestCase() throws Exception {

        TestCaseTimestamp testCaseOne = this.createTimestamp("0000000000000001",DEFAULT_TIME);

        this.store.insertTestCase(testCaseOne);

        TestCaseTimestamp retrievedTestCase = this.store.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), retrievedTestCase.getMessageNumber());
        assertEquals(testCaseOne.getTimestamp(), new Long(retrievedTestCase.getTimestamp()/1000));

    }

    @Test
    public void dropAllTestCases() throws Exception {

        TestCaseTimestamp testCaseOne = this.createTimestamp("0000000000000001",DEFAULT_TIME);
        TestCaseTimestamp testCaseTwo = this.createTimestamp("0000000000000002",DEFAULT_TIME);

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        this.store.dropAllTestCases();

        List<TestCaseTimestamp> timestamps = this.store.getAllTestCases();
        assertTrue(timestamps.isEmpty());

    }

    @Test
    public void findTestCase() throws Exception {

        TestCaseTimestamp testCaseOne = this.createTimestamp("0000000000000001",DEFAULT_TIME);
        TestCaseTimestamp testCaseTwo = this.createTimestamp("0000000000000002",DEFAULT_TIME);

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        TestCaseTimestamp timestamp = this.store.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseOne.getTimestamp(), new Long(timestamp.getTimestamp()/1000));

        timestamp = this.store.findTestCase(testCaseTwo.getMessageNumber());
        assertEquals(testCaseTwo.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseTwo.getTimestamp(), new Long(timestamp.getTimestamp()/1000));

    }

    @Test
    public void toTestCaseTimestamp() {

        String messageNumber = "0000000000000001";
        String messageType = "BOMMAT04";
        String timestamp = "2016-12-30T08:33:33.873-07:00";

        BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
        builder.append(TestCaseDataStore.MESSAGE_NUMBER, messageNumber);
        builder.append(TestCaseDataStore.MESSAGE_TYPE, messageType);
        builder.append(TestCaseDataStore.MESSAGE_TIMESTAMP, timestamp);

        TestCaseTimestamp tct = this.store.toTestCaseTimestamp(builder.get());

        assertEquals(messageNumber, tct.getMessageNumber());
        assertEquals(messageType, tct.getMessageType());
//        assertEquals(new Long(1483112013873L), tct.getTimestamp());
        System.out.println(tct.getTimestamp());

    }

    private TestCaseTimestamp createTimestamp(final String mNumber, final Long timestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setTimestamp(timestamp);

        return tct;

    }

}