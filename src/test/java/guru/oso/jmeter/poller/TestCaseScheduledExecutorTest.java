package guru.oso.jmeter.poller;

import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;
import guru.oso.jmeter.data.TestCaseTimestampDAOMySQL;
import guru.oso.jmeter.props.PropertiesResolver;

import guru.oso.jmeter.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BC on 12/26/16.
 */
public class TestCaseScheduledExecutorTest {

    private static int POLLING_TIME = 5;

    private TestCaseTimestampDAO dao;
    private TestCaseTimestamp expectedTimestamp;

    @Before
    public void setUp() throws Exception {

        PropertiesResolver resolver = new PropertiesResolver("src/main/resources/test.properties");
        Properties props = resolver.getProperties();

        String mysql_host = props.getProperty("MYSQL_HOST");

        this.dao = new TestCaseTimestampDAOMySQL(mysql_host);
        //        this.dao = new TestCaseTimestampDAOMongo("mule_user", "mule_user", "localhost", "mule_perf_test");

        String uuid = UUID.randomUUID().toString();

        this.expectedTimestamp = TestUtils.createTimestamp(uuid);

    }

    @After
    public void tearDown() throws Exception {

        this.dao.dropAllTestCases();
        this.dao = null;
        this.expectedTimestamp = null;

    }

    @Test
    public void scheduleReal() throws Exception {

        this.dao.insertTestCase(this.expectedTimestamp);

        TestCaseScheduledExecutor executor = new TestCaseScheduledExecutor(this.expectedTimestamp.getMessageNumber(), this.dao);
        TestCaseTimestamp timestamp = executor.pollForTestCase(POLLING_TIME);

        System.out.println("Real Test Case");
        assertTrue(timestamp instanceof RealTestCaseTimestamp);
        assertEquals(this.expectedTimestamp.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(this.expectedTimestamp.getMessageType(), timestamp.getMessageType());
        assertEquals(this.expectedTimestamp.getTimestamp(), timestamp.getTimestamp());


    }

    @Test
    public void scheduleNull() throws Exception {

        this.dao.insertTestCase(this.expectedTimestamp);

        TestCaseScheduledExecutor executor = new TestCaseScheduledExecutor("null", this.dao);
        TestCaseTimestamp timestamp = executor.pollForTestCase(POLLING_TIME);

        System.out.println("Null Test Case");
        assertTrue(timestamp instanceof NullTestCaseTimeStamp);
        assertEquals(TestCaseTimestamp.NULL_MESSAGE_NUMBER, timestamp.getMessageNumber());
        assertEquals(TestCaseTimestamp.NULL_MESSAGE_TYPE, timestamp.getMessageType());
        assertEquals(TestCaseTimestamp.NULL_TIMESTAMP, timestamp.getTimestamp());

    }

}