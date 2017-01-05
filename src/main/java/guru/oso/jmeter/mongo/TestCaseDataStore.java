package guru.oso.jmeter.mongo;

import com.mongodb.*;

import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestDataStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BC on 12/25/16.
 */
public class TestCaseDataStore implements TestDataStore {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseDataStore.class);

    private final MongoClientURI mongoClientURI;
    private MongoClient dbClient;

    private static final String DATABASE_NAME = "mule_perf_test";
    private static final String COLLECTION_NAME = "Threads";

    public TestCaseDataStore(final String user, final String pwd, final String host, final String authDB) {

//        MongoClientURI mongoURI = new MongoClientURI("mongodb://mule_user:mule_user@localhost/?authSource=mule_perf_test&authMechanism=MONGODB-CR");

        StringBuilder dbBuilder = new StringBuilder("mongodb://");
        dbBuilder.append(user + ":" + pwd + "@" + host +"/");
        dbBuilder.append("?authSource=" + authDB + "&authMechanism=MONGODB-CR");
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
                timestampList.add(MongoUtilities.toTestCaseTimestamp(cursor.next()));
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

            DBObject dbObj = MongoUtilities.toDBObject(timestamp);
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
                tct = MongoUtilities.toTestCaseTimestamp(dbObj);
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

}