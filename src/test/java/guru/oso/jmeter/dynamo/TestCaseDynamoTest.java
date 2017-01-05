package guru.oso.jmeter.dynamo;

import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestDataStore;
import guru.oso.jmeter.props.PropertiesResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BC on 1/4/17.
 */
public class TestCaseDynamoTest {

    public static final long DEFAULT_TIME = 1483115162199L;

    private TestDataStore store;

    @Before
    public void setUp() throws Exception {

        PropertiesResolver resolver = new PropertiesResolver("src/main/resources/test.properties");
        Properties props = resolver.getProperties();

        String accessKey = props.getProperty(PropertiesResolver.ACCESS_KEY);
        String secretKey = props.getProperty(PropertiesResolver.SECRET_KEY);

        this.store = new TestCaseDynamo(accessKey, secretKey);

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
            assertEquals(DEFAULT_TIME, timestamp.getTimestamp().longValue());
            assertEquals(TestDataStore.MESSAGE_TYPE, timestamp.getMessageType());
        }

    }

    @Test
    public void insertTestCase() throws Exception {

        TestCaseTimestamp testCaseOne = this.createTimestamp("0000000000000001",DEFAULT_TIME);

        this.store.insertTestCase(testCaseOne);

        TestCaseTimestamp retrievedTestCase = this.store.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), retrievedTestCase.getMessageNumber());
        assertEquals(testCaseOne.getTimestamp(), retrievedTestCase.getTimestamp());


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
        assertEquals(testCaseOne.getTimestamp(), timestamp.getTimestamp());

        timestamp = this.store.findTestCase(testCaseTwo.getMessageNumber());
        assertEquals(testCaseTwo.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseTwo.getTimestamp(), timestamp.getTimestamp());

    }

    private TestCaseTimestamp createTimestamp(final String mNumber, final Long timestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(TestDataStore.MESSAGE_TYPE);
        tct.setTimestamp(timestamp);

        return tct;

    }

}