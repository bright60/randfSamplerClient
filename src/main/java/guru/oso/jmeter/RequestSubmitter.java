package guru.oso.jmeter;

import guru.oso.jmeter.data.TestCaseTimestamp;
import guru.oso.jmeter.data.TestCaseTimestampDAO;
import guru.oso.jmeter.dom.IDocDOM;
import guru.oso.jmeter.http.IDocHTTPClient;
import guru.oso.jmeter.data.TestCaseTimestampDAOMySQL;
import guru.oso.jmeter.poller.TestCaseScheduledExecutor;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by BC on 1/3/17.
 */
public class RequestSubmitter {

    public static final String FILE = "FILE";
    public static final String HOST = "HOST";
    public static final String MYSQL_HOST = "MYSQL_HOST";
    public static final String POLLER_DELAY = "POLLER_DELAY";

    public static final String CORRELATION_ID = "CORRELATION_ID";

    private static final Logger logger = LoggerFactory.getLogger(RequestSubmitter.class);

//    public static final String PROPERTIES_FILE = "test.properties";

    public static Long timeRequest(final String messageNumber, final Map<String,String> params) {

        long currentTime = System.currentTimeMillis();

        String payloadPath = params.get(FILE);
        String bodyXML = new IDocDOM(payloadPath).toXML();
        String response = submitRequest(bodyXML, messageNumber, params);

        String mysql_host = params.get(MYSQL_HOST);
        TestCaseTimestampDAO dataStore = new TestCaseTimestampDAOMySQL(mysql_host);

        TestCaseScheduledExecutor executor = new TestCaseScheduledExecutor(messageNumber, dataStore);

        String delay = params.get(POLLER_DELAY);
        TestCaseTimestamp timestamp = executor.pollForTestCase(Integer.parseInt(delay));

        logger.info("MessageNumber: " + timestamp.getMessageNumber());
        logger.info("MessageType: " + timestamp.getMessageType());

        long endTime = timestamp.getTimestamp();

        Instant startInstant = Instant.ofEpochMilli(currentTime);
        Instant finishedInstant = Instant.ofEpochMilli(endTime);
        Duration duration = Duration.between(startInstant, finishedInstant);

        logger.info("Current: " + currentTime + " After: " + endTime);
        logger.info("Duration: " + duration.toMillis() + "ms");

        logger.info("Response: " + response);

        return endTime;

    }

    private static String submitRequest(final String idocXML, final String messageNumber, final Map<String,String> params) {

        Header contentHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
        Header corrlationHeader = new BasicHeader(CORRELATION_ID, messageNumber);
        List<Header> headers = new ArrayList<>();
        headers.add(contentHeader);
        headers.add(corrlationHeader);

        HttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();

        String host = params.get(HOST);
        HttpUriRequest request = IDocHTTPClient.generatePostRequest(idocXML, host);

        String result = "";
        try {

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            } else {
                System.out.println("Entity is null!");
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return result;

    }

}
