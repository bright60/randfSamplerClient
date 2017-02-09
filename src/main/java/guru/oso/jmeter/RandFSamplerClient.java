package guru.oso.jmeter;

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
public class RandFSamplerClient extends AbstractJavaSamplerClient {

    private static Logger logger = LoggerFactory.getLogger(RandFSamplerClient.class);

    private Map<String, String> mapParams = new HashMap<String, String>();

    public RandFSamplerClient() {
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
            Long endTime = RequestSubmitter.timeRequest(uuid.toString(), mapParams);

            result.sampleEnd();
            result.setEndTime(endTime);

            if (endTime < 0) {
                result.setSuccessful(false);
                result.setSampleLabel("FAILURE");
            }

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

        params.addArgument("HOST", "https://test-3pl-logistics-api-20170201.cloudhub.io/rest/api/v/0/1/delivery/confirmation");
        params.addArgument("POLLER_DELAY", "5");
        params.addArgument("FILE", "../file/DeliveryConfirmation_Request.xml");
        params.addArgument("MYSQL_HOST", "104.154.236.96");

        return params;

    }

}
