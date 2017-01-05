package guru.oso.jmeter.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by BC on 12/28/16.
 */
public class PropertiesResolver {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesResolver.class);

    // Keys
    public static final String BOMMATO4 = "BOMMAT04_IDoc_File";
    public static final String DELVRY07 = "DELVRY07_IDoc_File";
    public static final String MATMAS05 = "MATMAS05_IDoc_File";
    public static final String ZDELVRY03 ="ZDELVRY03_IDoc_File";
    public static final String SCHEME_AND_HOST ="SCHEME_AND_HOST";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String SECRET_KEY = "SECRET_KEY";

    private Properties properties;

    public PropertiesResolver(final String propertiesPath) {

        properties = resolveProperties(propertiesPath);

    }

    public Properties getProperties() {
        return properties;
    }

    private Properties resolveProperties(final String propertiesPath) {

//        String propertiesPath = "test.properties";

        Properties props = new Properties();

        try (InputStream in = new FileInputStream(propertiesPath)) {

            props.load(in);

        } catch (IOException ioe) {
            logger.error("Cannot load file.", ioe);
            ioe.printStackTrace();
        }

        return props;

    }

}
