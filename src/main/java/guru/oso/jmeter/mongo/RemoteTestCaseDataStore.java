package guru.oso.jmeter.mongo;

import com.mongodb.*;
import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BC on 1/4/17.
 */
public class RemoteTestCaseDataStore implements TestDataStore {

    private static final Logger logger = LoggerFactory.getLogger(RemoteTestCaseDataStore.class);

    private final MongoClientURI mongoClientURI;
    private MongoClient dbClient;

    private static final String DATABASE_NAME = "remote-test-jmeter";
    private static final String COLLECTION_NAME = "Threads";

    public RemoteTestCaseDataStore(final String usr, final String pwd) {

//        mongodb://<dbuser>:<dbpassword>@ds155028.mlab.com:55028/remote-test-jmeter

        StringBuilder dbBuilder = new StringBuilder("mongodb://");
        dbBuilder.append(usr + ":" + pwd + "@ds155028.mlab.com:55028/remote-test-jmeter");
        String mongoURI = dbBuilder.toString();

        logger.info("MongoURI: " + mongoURI);

        mongoClientURI = new MongoClientURI(mongoURI);

    }

    @Override
    public List<TestCaseTimestamp> getAllTestCases() {

        List<TestCaseTimestamp> timestampList = new ArrayList<>();

        try {

            DB mongoDatabase = this.getMongoDatabase();
            DBCollection collection = mongoDatabase.getCollection(COLLECTION_NAME);

            DBCursor cursor = collection.find();

            while (cursor.hasNext()) {
                timestampList.add(this.toTestCaseTimestamp(cursor.next()));
            }


        } catch (UnknownHostException uhe) {
            logger.error("Unknown Host.", uhe);
        } finally {
            this.dbClient.close();
        }

        return timestampList;

    }

    @Override
    public void insertTestCase(TestCaseTimestamp timestamp) {

        try {

            DB mongoDatabase = this.getMongoDatabase();
            DBCollection collection = mongoDatabase.getCollection(COLLECTION_NAME);

            DBObject dbObj = this.toDBObject(timestamp);
            collection.insert(dbObj);

        } catch (UnknownHostException uhe) {
            logger.error("Unknown Host.", uhe);
        } finally {
            this.dbClient.close();
        }

    }

    @Override
    public void dropAllTestCases() {

        try {

            DB mongoDatabase = this.getMongoDatabase();
            DBCollection collection = mongoDatabase.getCollection(COLLECTION_NAME);

            collection.drop();

        } catch (UnknownHostException uhe) {
            logger.error("Unknown Host.", uhe);
        } finally {
            this.dbClient.close();
        }

    }

    @Override
    public TestCaseTimestamp findTestCase(final String messageNumber) {

        TestCaseTimestamp tct = new NullTestCaseTimeStamp();

        try {

            DB mongoDatabase = this.getMongoDatabase();
            DBCollection collection = mongoDatabase.getCollection(COLLECTION_NAME);

            BasicDBObject whereQuery = new BasicDBObject();
            whereQuery.put(MESSAGE_NUMBER,messageNumber);

            DBObject dbObj = collection.findOne(whereQuery);

            if (dbObj != null) {
                tct = this.toTestCaseTimestamp(dbObj);
            }

            return tct;

        } catch (UnknownHostException uhe) {
            logger.error("Unknown Host.", uhe);
        } finally {
            this.dbClient.close();
        }

        return tct;

    }

    private DB getMongoDatabase() throws UnknownHostException {

        this.dbClient = new MongoClient(mongoClientURI);
        return this.dbClient.getDB(DATABASE_NAME);

    }

    protected TestCaseTimestamp toTestCaseTimestamp(final DBObject dbObj) {

        TestCaseTimestamp tct = new RealTestCaseTimestamp();
        tct.setMessageNumber((String) dbObj.get(MESSAGE_NUMBER));
        tct.setMessageType((String) dbObj.get(MESSAGE_TYPE));

        String timestampString = (String) dbObj.get(MESSAGE_TIMESTAMP);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse((String) timestampString, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        tct.setTimestamp(zonedDateTime.toInstant().toEpochMilli());

        return tct;

    }

    private DBObject toDBObject(final TestCaseTimestamp tct) {

        String messageNumber = tct.getMessageNumber();
        String messageType = tct.getMessageType();
        long timestamp = tct.getTimestamp();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        String formattedDateTime = zdt.format(formatter);

        return new BasicDBObjectBuilder().append(MESSAGE_NUMBER, messageNumber).append(MESSAGE_TYPE, messageNumber).append(MESSAGE_TIMESTAMP, formattedDateTime).get();

    }

}
