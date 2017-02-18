package guru.oso.jmeter.data;

/**
 * Created by BC on 12/27/16.
 */
public class RealTestCaseTimestamp extends TestCaseTimestamp {

    @Override
    public String getMessageNumber() {
        return messageNumber;
    }

    @Override
    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    @Override
    public String getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public Long getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Long timestamp) {
        this.startTime = timestamp;

    }

    @Override
    public Long getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Long timestamp) {
        this.endTime = timestamp;
    }

}
