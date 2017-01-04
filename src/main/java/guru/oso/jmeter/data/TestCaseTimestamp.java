package guru.oso.jmeter.data;

/**
 * Created by BC on 12/26/16.
 */
public abstract class TestCaseTimestamp {

    public static final String NULL_MESSAGE_NUMBER = "-1";
    public static final String NULL_MESSAGE_TYPE = "NULL";
    public static final Long NULL_TIMESTAMP = -1L;

    protected String messageNumber;
    protected String messageType;
    protected Long timestamp;

    public abstract String getMessageNumber();

    public abstract void setMessageNumber(String messageNumber);

    public abstract String getMessageType();

    public abstract void setMessageType(String messageType);

    public abstract Long getTimestamp();

    public abstract void setTimestamp(Long timestamp);

    @Override
    public String toString() {
        return "TestCaseTimestamp{" +
                "messageNumber='" + messageNumber + '\'' +
                "messageType='" + messageType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
