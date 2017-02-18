package guru.oso.jmeter.data;

import java.util.List;

/**
 * Created by BC on 1/4/17.
 */
public interface TestCaseTimestampDAO {

    String MESSAGE_NUMBER = "messageNumber";
    String MESSAGE_TYPE = "messageType";
    String MESSAGE_END_TIME = "messageEndTime";
    String MESSAGE_START_TIME = "messageStartTime";

    List<TestCaseTimestamp> getAllTestCases();

    void insertTestCase(TestCaseTimestamp timestamp);

    void dropAllTestCases();

    TestCaseTimestamp findTestCase(final String messageNumber);

}
