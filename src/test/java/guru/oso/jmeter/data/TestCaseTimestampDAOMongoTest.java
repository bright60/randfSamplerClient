package guru.oso.jmeter.data;

import com.mongodb.BasicDBObjectBuilder;
import guru.oso.jmeter.props.PropertiesResolver;
import guru.oso.jmeter.util.MongoUtilities;
import guru.oso.jmeter.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BC on 12/25/16.
 */
public class TestCaseTimestampDAOMongoTest {

    private TestCaseTimestampDAO store;

    @Before
    public void setUp() throws Exception {

        Properties props = new PropertiesResolver("src/test/resources/test.properties").getProperties();

        String mongoURI = props.getProperty("MONGO_URI");

        this.store = new TestCaseTimestampDAOMongo(mongoURI);

    }

    @After
    public void tearDown() throws Exception {

        this.store.dropAllTestCases();

        this.store = null;

    }

    @Test
    public void getAllTestCases() throws Exception {

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp("0000000000000001");
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp("0000000000000002");

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        List<TestCaseTimestamp> timestamps = this.store.getAllTestCases();
        assertEquals(2, timestamps.size());
        for (TestCaseTimestamp timestamp : timestamps) {

            System.out.println("MessageNumber:" + timestamp.getMessageNumber() + " StartTime:" + timestamp.getStartTime() + " EndTime:" + timestamp.getEndTime());

        }

    }

    @Test
    public void insertTestCase() throws Exception {

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp("0000000000000001");

        this.store.insertTestCase(testCaseOne);

        TestCaseTimestamp retrievedTestCase = this.store.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), retrievedTestCase.getMessageNumber());
        assertEquals(testCaseOne.getStartTime(), retrievedTestCase.getStartTime());
        assertEquals(testCaseOne.getEndTime(), retrievedTestCase.getEndTime());

    }

    @Test
    public void dropAllTestCases() throws Exception {

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp("0000000000000001");
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp("0000000000000002");

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        this.store.dropAllTestCases();

        List<TestCaseTimestamp> timestamps = this.store.getAllTestCases();
        assertTrue(timestamps.isEmpty());

    }

    @Test
    public void findTestCase() throws Exception {

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp("0000000000000001");
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp("0000000000000002");

        this.store.insertTestCase(testCaseOne);
        this.store.insertTestCase(testCaseTwo);

        TestCaseTimestamp timestamp = this.store.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseOne.getStartTime(), timestamp.getStartTime());
        assertEquals(testCaseOne.getEndTime(), timestamp.getEndTime());

        timestamp = this.store.findTestCase(testCaseTwo.getMessageNumber());
        assertEquals(testCaseTwo.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseTwo.getStartTime(), timestamp.getStartTime());
        assertEquals(testCaseTwo.getEndTime(), timestamp.getEndTime());

    }

    @Test
    public void toTestCaseTimestamp() {

        String messageNumber = "0000000000000001";
        String messageType = "BOMMAT04";
        Long starttime = 1487052355245L;
        Long endtime = 1487052356242L;

        BasicDBObjectBuilder builder = new BasicDBObjectBuilder();
        builder.append(TestCaseTimestampDAO.MESSAGE_NUMBER, messageNumber);
        builder.append(TestCaseTimestampDAO.MESSAGE_TYPE, messageType);
        builder.append(TestCaseTimestampDAO.MESSAGE_START_TIME, starttime);
        builder.append(TestCaseTimestampDAO.MESSAGE_END_TIME, endtime);

        TestCaseTimestamp tct = MongoUtilities.toTestCaseTimestamp(builder.get());

        assertEquals(messageNumber, tct.getMessageNumber());
        assertEquals(messageType, tct.getMessageType());
//        assertEquals(new Long(1483112013873L), tct.getTimestamp());
        System.out.println(tct.getStartTime());
        System.out.println(tct.getEndTime());

    }

}