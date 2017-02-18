package guru.oso.jmeter.data;

import com.mongodb.*;
import guru.oso.jmeter.util.MongoUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BC on 12/25/16.
 */
public class TestCaseTimestampDAOMongo implements TestCaseTimestampDAO {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseTimestampDAOMongo.class);

    private final MongoClientURI mongoClientURI;
    private MongoClient dbClient;

    private static final String DATABASE_NAME = "remote-test-jmeter";
    private static final String COLLECTION_NAME = "randf_txn";

    public TestCaseTimestampDAOMongo(final String mongoURI) {

//        mongodb://<dbuser>:<dbpassword>@ds155028.mlab.com:55028/remote-test-jmeter

        logger.info("MongoURI: " + mongoURI);

        mongoClientURI = new MongoClientURI(mongoURI);

    }

    public TestCaseTimestampDAOMongo(final String user, final String pwd, final String host, final String authDB) {

//        mongodb://<dbuser>:<dbpassword>@ds155028.mlab.com:55028/remote-test-jmeter

//        MongoClientURI mongoURI = new MongoClientURI("mongodb://mule_user:mule_user@localhost/?authSource=mule_perf_test&authMechanism=MONGODB-CR");

        StringBuilder dbBuilder = new StringBuilder("mongodb://");
        dbBuilder.append(user + ":" + pwd + "@" + host +"/");
//        dbBuilder.append("?authSource=" + authDB + "&authMechanism=MONGODB-CR");
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

            logger.info("DAO:Start:" + tct.getStartTime(), tct.getEndTime());

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
