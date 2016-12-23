package guru.oso.jmeter;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by benjamincurrier on 12/23/16.
 */
public class RandFSamplerClient extends AbstractJavaSamplerClient {

    private Map<String, String> mapParams = new HashMap<String, String>();

    public RandFSamplerClient() {
        super();
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        for (Iterator<String> it = context.getParameterNamesIterator(); it.hasNext(); ) {
            String paramName = it.next();
            mapParams.put(paramName, context.getParameter(paramName));
        }
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();

        try {

            JMeterVariables vars = JMeterContextService.getContext().getVariables();
            vars.put("demo", "demoVariableContent");

            result.sampleStart();

            TimeUnit.SECONDS.sleep(2);

//            Student student = new Student();
//            student.setStudentname(mapParams.get("name")+" "+new Date().getTime());
//            Session session = sessionFactory.openSession();
//            session.save(student);
//            session.flush();
//            session.close();

            result.sampleEnd();


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

        params.addArgument("name", "edw");

        return params;
    }

}
