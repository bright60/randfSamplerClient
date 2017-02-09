package guru.oso.jmeter;

import static guru.oso.jmeter.RequestSubmitter.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by BC on 2/7/17.
 */
public class RequestSubmitterTest {


    private Map<String,String> params;

    @Before
    public void setUp() throws Exception {

        params = new HashMap<>();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void timeRequestDeliveryConfirmation() throws Exception {

        String uuidString = UUID.randomUUID().toString();

        params.put(POLLER_DELAY, "5");
        params.put(MYSQL_HOST, "104.154.236.96");
        params.put(FILE, "src/test/resources/3pl_request/DeliveryConfirmation_Request.xml");
        params.put(HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/delivery/confirmation");

        System.out.println("UUID: " + uuidString);
        System.out.println("POLLER_DELAY: " + params.get(POLLER_DELAY));
        System.out.println("MYSQL_HOST: " + params.get(MYSQL_HOST));
        System.out.println("FILE: " + params.get(FILE));
        System.out.println("HOST: " + params.get(HOST));

        RequestSubmitter.timeRequest(uuidString, params);

    }

    @Test
    public void timeRequestGoodsReceipt() throws Exception {

        String uuidString = UUID.randomUUID().toString();

        params.put(POLLER_DELAY, "5");
        params.put(MYSQL_HOST, "104.154.236.96");
        params.put(FILE, "src/test/resources/3pl_request/GoodsReceipt_Request.xml");
        params.put(HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/goodsreceipt");

        System.out.println("UUID: " + uuidString);
        System.out.println("POLLER_DELAY: " + params.get(POLLER_DELAY));
        System.out.println("MYSQL_HOST: " + params.get(MYSQL_HOST));
        System.out.println("FILE: " + params.get(FILE));
        System.out.println("HOST: " + params.get(HOST));

        RequestSubmitter.timeRequest(uuidString, params);

    }

    @Test
    public void timeRequestInventorySnapshot() throws Exception {

        String uuidString = UUID.randomUUID().toString();

        params.put(POLLER_DELAY, "5");
        params.put(MYSQL_HOST, "104.154.236.96");
        params.put(FILE, "src/test/resources/3pl_request/InventorySnapshot_Request.xml");
        params.put(HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/inventory/snapshot");

        System.out.println("UUID: " + uuidString);
        System.out.println("POLLER_DELAY: " + params.get(POLLER_DELAY));
        System.out.println("MYSQL_HOST: " + params.get(MYSQL_HOST));
        System.out.println("FILE: " + params.get(FILE));
        System.out.println("HOST: " + params.get(HOST));

        RequestSubmitter.timeRequest(uuidString, params);

    }

    @Test
    public void timeRequestInventoryTransaction() throws Exception {

        String uuidString = UUID.randomUUID().toString();

        params.put(POLLER_DELAY, "5");
        params.put(MYSQL_HOST, "104.154.236.96");
        params.put(FILE, "src/test/resources/3pl_request/InventoryTransaction_Request.xml");
        params.put(HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/inventory/transaction");

        System.out.println("UUID: " + uuidString);
        System.out.println("POLLER_DELAY: " + params.get(POLLER_DELAY));
        System.out.println("MYSQL_HOST: " + params.get(MYSQL_HOST));
        System.out.println("FILE: " + params.get(FILE));
        System.out.println("HOST: " + params.get(HOST));

        RequestSubmitter.timeRequest(uuidString, params);

    }

    @Test
    public void timeRequestOrderStatus() throws Exception {

        String uuidString = UUID.randomUUID().toString();

        params.put(POLLER_DELAY, "5");
        params.put(MYSQL_HOST, "104.154.236.96");
        params.put(FILE, "src/test/resources/3pl_request/OrderStatus_Request.xml");
        params.put(HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/delivery/orderstatus");

        System.out.println("UUID: " + uuidString);
        System.out.println("POLLER_DELAY: " + params.get(POLLER_DELAY));
        System.out.println("MYSQL_HOST: " + params.get(MYSQL_HOST));
        System.out.println("FILE: " + params.get(FILE));
        System.out.println("HOST: " + params.get(HOST));

        RequestSubmitter.timeRequest(uuidString, params);

    }

}