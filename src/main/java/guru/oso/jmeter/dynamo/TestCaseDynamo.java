package guru.oso.jmeter.dynamo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.*;

import guru.oso.jmeter.data.NullTestCaseTimeStamp;
import guru.oso.jmeter.data.RealTestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestDataStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by BC on 1/4/17.
 */
public class TestCaseDynamo implements TestDataStore {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseDynamo.class);

    private static final String TABLE_NAME = "Performance";

    private AmazonDynamoDBClient client;
    private DynamoDB db;
    private Table table;

    public TestCaseDynamo() {

        String access = "AKIAIUSH3KWK7ZPRAI6Q";
            String secret = "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";
        AWSCredentials credentials = new BasicAWSCredentials(access, secret);
        this.client = new AmazonDynamoDBClient(credentials);
        this.db = new DynamoDB(client);
        this.table = this.createTable();

    }

    @Override
    public List<TestCaseTimestamp> getAllTestCases() {

        List<TestCaseTimestamp> tStamps = new ArrayList<>();

        if (table == null) {
            table = this.createTable();
        }

        ScanRequest scanRequest = new ScanRequest().withTableName(TABLE_NAME);

        ScanResult result = this.client.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()){

            Set<Map.Entry<String, AttributeValue>> entries = item.entrySet();
            logger.info("Number of Entries:" + entries.size());
            tStamps.add(this.toTestCaseTimestamp(entries));

        }

        return tStamps;

    }

    @Override
    public void insertTestCase(TestCaseTimestamp timestamp) {

        if (this.table == null) {
            this.table = this.createTable();
        } else {
            logger.info("Table:" + table.getTableName());
        }

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table.putItem(new Item()
                    .withPrimaryKey(TestDataStore.MESSAGE_NUMBER, timestamp.getMessageNumber())
                    .withLong(TestDataStore.MESSAGE_TIMESTAMP, timestamp.getTimestamp())
                    .withString(TestDataStore.MESSAGE_TYPE, timestamp.getMessageType()));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        } catch (Exception e) {
            logger.error("Unable to add item: " + timestamp.getMessageNumber(), e);
        }

    }

    @Override
    public void dropAllTestCases() {

        if (table != null) {
            table.delete();

            try {
                table.waitForDelete();
            } catch (InterruptedException ie) {
                logger.error("Delete interrupted!", ie);
            }
            table = null;
        }

    }

    @Override
    public TestCaseTimestamp findTestCase(String messageNumber) {

        if (table == null) {
            table = this.createTable();
        }

        GetItemSpec spec = new GetItemSpec().withPrimaryKey(MESSAGE_NUMBER, messageNumber);

        TestCaseTimestamp timestamp;

        try {
            logger.info("Attempting to read the item...");
            Item outcome = table.getItem(spec);
            logger.info("GetItem succeeded: " + outcome);
            timestamp = this.toTestCaseTimestamp(outcome);
        } catch (Exception e) {
            logger.error("Unable to read item: " + messageNumber, e);
            timestamp = new NullTestCaseTimeStamp();
        }

        return timestamp;

    }

    private Table createTable() {

        Table table;

        try {

            logger.info("Attempting to create table; please wait...");
            table = this.db.createTable(TABLE_NAME,
                    Arrays.asList(new KeySchemaElement(MESSAGE_NUMBER, KeyType.HASH)),
                    Arrays.asList(new AttributeDefinition(MESSAGE_NUMBER, ScalarAttributeType.S)),
                    new ProvisionedThroughput(10L, 10L));
            table.waitForActive();
            logger.info("Success.  Table status: " + table.getDescription().getTableStatus());

        } catch (Exception e) {
            logger.error("Unable to create table: ", e);
            table = null;
        }

        return table;

    }

    private TestCaseTimestamp toTestCaseTimestamp(final Item item) {

        TestCaseTimestamp timestamp;

        if (item != null) {
            timestamp = new RealTestCaseTimestamp();
            timestamp.setMessageNumber(item.getString(MESSAGE_NUMBER));
            timestamp.setMessageType(item.getString(MESSAGE_TYPE));
            timestamp.setTimestamp(new Long(item.getString(MESSAGE_TIMESTAMP)));
        } else {
            timestamp = new NullTestCaseTimeStamp();
        }

        return timestamp;

    }

    private TestCaseTimestamp toTestCaseTimestamp(final Set<Map.Entry<String, AttributeValue>> entries) {

        TestCaseTimestamp timestamp = new RealTestCaseTimestamp();

        if(entries != null) {

            for(Map.Entry<String,AttributeValue> entry : entries) {

                if (entry.getKey().equals(MESSAGE_NUMBER)) {
                    timestamp.setMessageNumber(entry.getValue().getS());
                } else if (entry.getKey().equals(MESSAGE_TYPE)) {
                    timestamp.setMessageType(entry.getValue().getS());
                } else if (entry.getKey().equals(MESSAGE_TIMESTAMP)) {
                    timestamp.setTimestamp(new Long(entry.getValue().getN()));
                }

            }

        } else {
            timestamp = new NullTestCaseTimeStamp();
        }

        return timestamp;

    }

}
