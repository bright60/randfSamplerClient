package guru.oso.jmeter.util;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by BC on 1/4/17.
 */
public class MongoUtilities {

    /* Only for later versions of the MongoDB driver.

    public static TestCaseTimestamp toTestCaseTimestamp(final Document document) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber(document.getString(TestCaseTimestampDAO.MESSAGE_NUMBER));
        tct.setMessageType(document.getString(TestCaseTimestampDAO.MESSAGE_TYPE));

        String timestampString = document.getString(TestCaseTimestampDAO.MESSAGE_TIMESTAMP);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse((String) timestampString, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        tct.setTimestamp(zonedDateTime.toInstant().toEpochMilli());

        return tct;

    }

    public static Document toDocument(final TestCaseTimestamp tct) {

        String messageNumber = tct.getMessageNumber();
        String messageType = tct.getMessageType();
        long timestamp = tct.getTimestamp();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        String formattedDateTime = zdt.format(formatter);

        return new Document(TestCaseTimestampDAO.MESSAGE_NUMBER, messageNumber).append(TestCaseTimestampDAO.MESSAGE_TYPE, messageNumber).append(TestCaseTimestampDAO.MESSAGE_TIMESTAMP, formattedDateTime);

    }
*/
    public static TestCaseTimestamp toTestCaseTimestamp(final DBObject dbObj) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber((String) dbObj.get(TestCaseTimestampDAO.MESSAGE_NUMBER));
        tct.setMessageType((String) dbObj.get(TestCaseTimestampDAO.MESSAGE_TYPE));

        String timestampString = (String) dbObj.get(TestCaseTimestampDAO.MESSAGE_TIMESTAMP);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse((String) timestampString, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        tct.setTimestamp(zonedDateTime.toInstant().toEpochMilli());

        return tct;

    }

    public static DBObject toDBObject(final TestCaseTimestamp tct) {

        String messageNumber = tct.getMessageNumber();
        String messageType = tct.getMessageType();
        long timestamp = tct.getTimestamp();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        String formattedDateTime = zdt.format(formatter);

        return new BasicDBObjectBuilder().append(TestCaseTimestampDAO.MESSAGE_NUMBER, messageNumber).append(TestCaseTimestampDAO.MESSAGE_TYPE, messageNumber).append(TestCaseTimestampDAO.MESSAGE_TIMESTAMP, formattedDateTime).get();

    }

}
