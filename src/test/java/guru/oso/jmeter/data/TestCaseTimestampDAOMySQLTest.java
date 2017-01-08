package guru.oso.jmeter.data;

import guru.oso.jmeter.props.PropertiesResolver;

import guru.oso.jmeter.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by BC on 1/7/17.
 */
public class TestCaseTimestampDAOMySQLTest {

    private TestCaseTimestampDAOMySQL database;
//    private TestCaseTimestampDAOMySQL database3;

    @Before
    public void setUp() throws Exception {

        Properties props = new PropertiesResolver("src/test/resources/test.properties").getProperties();

        String mysql_host = props.getProperty("MYSQL_HOST");

        this.database = new TestCaseTimestampDAOMySQL(mysql_host);
//        this.database3 = new TestCaseTimestampDAOMySQL(mysql_host, "root","");

    }

    @After
    public void tearDown() throws Exception {

        this.database.dropAllTestCases();

        this.database = null;
//        this.database3 = null;

    }

    @Test
    public void getAllTestCases() throws Exception {

        String uuidOne = UUID.randomUUID().toString();
        String uuidTwo = UUID.randomUUID().toString();

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp(uuidOne);
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp(uuidTwo);

        this.database.insertTestCase(testCaseOne);
        this.database.insertTestCase(testCaseTwo);

        List<TestCaseTimestamp> timestamps = this.database.getAllTestCases();
        assertEquals(2, timestamps.size());

    }

    @Test
    public void insertTestCase() throws Exception {

        String uuid = UUID.randomUUID().toString();

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp(uuid);

        this.database.insertTestCase(testCaseOne);

        TestCaseTimestamp retrievedTestCase = this.database.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), retrievedTestCase.getMessageNumber());
        assertEquals(testCaseOne.getTimestamp(), new Long(retrievedTestCase.getTimestamp()));

    }

    @Test
    public void dropAllTestCases() throws Exception {

        String uuidOne = UUID.randomUUID().toString();
        String uuidTwo = UUID.randomUUID().toString();

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp(uuidOne);
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp(uuidTwo);

        this.database.insertTestCase(testCaseOne);
        this.database.insertTestCase(testCaseTwo);

        this.database.dropAllTestCases();

        List<TestCaseTimestamp> timestamps = this.database.getAllTestCases();
        assertTrue(timestamps.isEmpty());

    }

    @Test
    public void findTestCase() throws Exception {

        String uuidOne = UUID.randomUUID().toString();
        String uuidTwo = UUID.randomUUID().toString();

        TestCaseTimestamp testCaseOne = TestUtils.createTimestamp(uuidOne);
        TestCaseTimestamp testCaseTwo = TestUtils.createTimestamp(uuidTwo);

        this.database.insertTestCase(testCaseOne);
        this.database.insertTestCase(testCaseTwo);

        TestCaseTimestamp timestamp = this.database.findTestCase(testCaseOne.getMessageNumber());
        assertEquals(testCaseOne.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseOne.getTimestamp(), new Long(timestamp.getTimestamp()));

        timestamp = this.database.findTestCase(testCaseTwo.getMessageNumber());
        assertEquals(testCaseTwo.getMessageNumber(), timestamp.getMessageNumber());
        assertEquals(testCaseTwo.getTimestamp(), new Long(timestamp.getTimestamp()));

    }

}