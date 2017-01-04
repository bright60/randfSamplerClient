package guru.oso.jmeter.data;

/**
 * Created by BC on 12/27/16.
 */
public class NullTestCaseTimeStamp extends TestCaseTimestamp {

    public String getMessageNumber() {
        return NULL_MESSAGE_NUMBER;
    }

    public void setMessageNumber(String messageNumber) {}

    public String getMessageType() {
        return NULL_MESSAGE_TYPE;
    }

    public void setMessageType(String messageType) {}

    public Long getTimestamp() {
        return NULL_TIMESTAMP;
    }

    public void setTimestamp(Long timestamp) {}

}
