package guru.oso.jmeter.data;

/**
 * Created by BC on 12/26/16.
 */
public abstract class TestCaseTimestamp {

    public static final String NULL_MESSAGE_NUMBER = "-1";
    public static final String NULL_MESSAGE_TYPE = "NULL";
    public static final Long NULL_TIMESTAMP = -1L;

    String messageNumber = NULL_MESSAGE_NUMBER;
    String messageType = NULL_MESSAGE_TYPE;
    Long startTime = NULL_TIMESTAMP;
    Long endTime = NULL_TIMESTAMP;

    public abstract String getMessageNumber();

    public abstract void setMessageNumber(String messageNumber);

    public abstract String getMessageType();

    public abstract void setMessageType(String messageType);

    public abstract Long getStartTime();

    public abstract void setStartTime(Long timestamp);

    public abstract Long getEndTime();

    public abstract void setEndTime(Long timestamp);

    @Override
    public String toString() {
        return "TestCaseTimestamp{" +
                "messageNumber='" + messageNumber + '\'' +
                ", messageType='" + messageType + '\'' +
                ", startTime=" + startTime + '\'' +
                ", endTime=" + endTime + '\'' +
                '}';
    }

}
