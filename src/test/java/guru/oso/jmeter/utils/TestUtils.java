package guru.oso.jmeter.utils;

import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;

/**
 * Created by BC on 1/8/17.
 */
public class TestUtils {

    public static final long DEFAULT_TIME = 1483115162199L;
    public static final String DEFAULT_TYPE = "IDOC";

    public static TestCaseTimestamp createTimestamp(final String mNumber) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(DEFAULT_TYPE);
        tct.setTimestamp(DEFAULT_TIME);

        return tct;

    }

    public static TestCaseTimestamp createTimestamp(final String mNumber, final String mType, final Long mTimestamp) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(mNumber);
        tct.setMessageType(mType);
        tct.setTimestamp(mTimestamp);

        return tct;

    }

}
