package guru.oso.jmeter.poller;

import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestDataStore;
import guru.oso.jmeter.dynamo.TestCaseDynamo;
import guru.oso.jmeter.props.PropertiesResolver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Created by BC on 12/26/16.
 */
public class TestCaseScheduledExecutorTest {

    private TestCaseScheduledExecutor executor;
    private TestDataStore store;
    private TestCaseTimestamp timestamp;

    @Before
    public void setUp() throws Exception {

        PropertiesResolver resolver = new PropertiesResolver("src/main/resources/test.properties");
        Properties props = resolver.getProperties();

        String accessKey = props.getProperty(PropertiesResolver.ACCESS_KEY);
        String secretKey = props.getProperty(PropertiesResolver.SECRET_KEY);

        this.store = new TestCaseDynamo(accessKey, secretKey);
        //        this.store = new TestCaseDataStore("mule_user", "mule_user", "localhost", "mule_perf_test");

        timestamp = this.createTimestamp("0000000000000001",1482533994L);
        this.executor = new TestCaseScheduledExecutor("0000000000000001", this.store);

    }

    @After
    public void tearDown() throws Exception {

        this.executor = null;
        this.store = null;
        this.timestamp = null;

    }

    @Test
    public void schedule() throws Exception {

        this.store.insertTestCase(timestamp);

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

//        this.store.dropAllTestCases();

    }

    private TestCaseTimestamp createTimestamp(final String mNumber, final Long timestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(TestDataStore.MESSAGE_TYPE);
        tct.setTimestamp(timestamp);

        return tct;

    }

}