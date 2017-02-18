package guru.oso.jmeter.utils;

import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;

/**
 * Created by BC on 1/8/17.
 */
public class TestUtils {

    public static final long DEFAULT_TIME = 1483115162199L;
    public static final String DEFAULT_TYPE = "DEFAULT_TYPE";

    public static TestCaseTimestamp createTimestamp(final String mNumber) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(DEFAULT_TYPE);
        tct.setStartTime(DEFAULT_TIME);
        tct.setEndTime(DEFAULT_TIME);

        return createTimestamp(mNumber, DEFAULT_TYPE, DEFAULT_TIME);

    }

    public static TestCaseTimestamp createTimestamp(final String mNumber, final String mType, final Long mTimestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(mType);
        tct.setStartTime(mTimestamp);
        tct.setEndTime(mTimestamp);

        return tct;

    }

}
