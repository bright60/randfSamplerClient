package guru.oso.jmeter.file;

import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;

import java.util.List;

/**
 * Created by BC on 1/4/17.
 */
public class TestCaseDataFile implements TestCaseTimestampDAO {



    @Override
    public List<TestCaseTimestamp> getAllTestCases() {
        return null;
    }

    @Override
    public void insertTestCase(TestCaseTimestamp timestamp) {

    }

    @Override
    public void dropAllTestCases() {

    }

    @Override
    public TestCaseTimestamp findTestCase(String messageNumber) {
        return null;
    }

}
