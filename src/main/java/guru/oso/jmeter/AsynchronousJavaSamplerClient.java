package guru.oso.jmeter;

import guru.oso.jmeter.data.TestCaseTimestamp;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by BC on 12/23/16.
 */
public class AsynchronousJavaSamplerClient extends AbstractJavaSamplerClient {

    private static Logger logger = LoggerFactory.getLogger(AsynchronousJavaSamplerClient.class);

    private Map<String, String> mapParams = new HashMap<String, String>();

    public AsynchronousJavaSamplerClient() {
        super();
    }

    @Override
    public void setupTest(JavaSamplerContext context) {

        for (Iterator<String> it = context.getParameterNamesIterator(); it.hasNext(); ) {
            String paramName = it.next();
            mapParams.put(paramName, context.getParameter(paramName));
            logger.info("Name:" + paramName + " Value:" + context.getParameter(paramName));
        }

    }

    public SampleResult runTest(JavaSamplerContext context) {

        SampleResult result = new SampleResult();

        try {

            JMeterVariables vars = JMeterContextService.getContext().getVariables();
//            vars.put("demo", "demoVariableContent");

            UUID uuid = UUID.randomUUID();
            result.sampleStart();
            TestCaseTimestamp ts = RequestSubmitter.timeRequest(uuid.toString() + " ", mapParams);

            result.sampleEnd();

            if (ts.getEndTime() < 0) {
                result.setSuccessful(false);
                result.setSampleLabel("FAILURE");
            }

            logger.info("Start: " + ts.getStartTime() + " End:" + ts.getEndTime());

            result = SampleResult.createTestSample(ts.getStartTime(), ts.getEndTime());
            result.setSuccessful(true);
            result.setSampleLabel("SUCCESS");

        } catch (Throwable e) {
            result.sampleEnd();
            result.setSampleLabel("FAILED: '" + e.getMessage() + "' || " + e.toString());
            result.setSuccessful(false);

            e.printStackTrace();
            System.out.println("\n\n\n");
        }

        return result;
    }

    @Override
    public Arguments getDefaultParameters() {

        Arguments params = new Arguments();

        params.addArgument(RequestSubmitter.HOST, "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/delivery/confirmation");
        params.addArgument(RequestSubmitter.POLLER_DELAY, "5");
        params.addArgument(RequestSubmitter.FILE, "../file/DeliveryConfirmation_Request.xml");
//        params.addArgument("MYSQL_HOST", "104.154.236.96");
        params.addArgument(RequestSubmitter.MONGO_URI, "mongodb://randf_user:randf_user@ds155028.mlab.com:55028/remote-test-jmeter");

        return params;

    }

}
