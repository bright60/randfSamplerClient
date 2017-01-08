package guru.oso.jmeter.data;

import java.util.List;

/**
 * Created by BC on 1/4/17.
 */
public interface TestCaseTimestampDAO {

    static final String MESSAGE_NUMBER = "messageNumber";
    static final String MESSAGE_TYPE = "messageType";
    static final String MESSAGE_TIMESTAMP = "timestamp";

    List<TestCaseTimestamp> getAllTestCases();

    void insertTestCase(TestCaseTimestamp timestamp);

    void dropAllTestCases();

    TestCaseTimestamp findTestCase(final String messageNumber);

}
