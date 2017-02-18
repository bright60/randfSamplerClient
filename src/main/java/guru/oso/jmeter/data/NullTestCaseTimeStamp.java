package guru.oso.jmeter.data;

/**
 * Created by BC on 12/27/16.
 */
public class NullTestCaseTimeStamp extends TestCaseTimestamp {

    @Override
    public String getMessageNumber() {
        return NULL_MESSAGE_NUMBER;
    }

    @Override
    public void setMessageNumber(String messageNumber) {
    }

    @Override
    public String getMessageType() {
        return NULL_MESSAGE_TYPE;
    }

    @Override
    public void setMessageType(String messageType) {
    }

    @Override
    public Long getStartTime() {
        return NULL_TIMESTAMP;
    }

    @Override
    public void setStartTime(Long timestamp) {
    }

    @Override
    public Long getEndTime() {
        return NULL_TIMESTAMP;
    }

    @Override
    public void setEndTime(Long timestamp) {

    }

}
