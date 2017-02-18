package guru.oso.jmeter.dom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Created by BC on 12/25/16.
 */
public class PayloadDOMTest {

    final String DEV_PROPS = "src/test/resources/test.properties";

    private static final String BOMMAT04_IDoc_File = "BOMMAT04_IDoc_File_Test";
    private static final String DELVRY07_IDoc_File = "DELVRY07_IDoc_File_Test";
    private static final String MATMAS05_IDoc_File = "MATMAS05_IDoc_File_Test";
    private static final String ZDELVRY03_IDoc_File = "ZDELVRY03_IDoc_File_Test";

    private Properties properties;


    private PayloadDOM BOMMAT04_IDoc_DOM;
    private PayloadDOM DELVRY07_IDoc_DOM;
    private PayloadDOM MATMAS05_IDoc_DOM;
    private PayloadDOM ZDELVRY03_IDoc_DOM;

    @Before
    public void runBefore() throws Exception {

        this.properties = new Properties();
        this.properties.load(new FileInputStream(DEV_PROPS));

        this.BOMMAT04_IDoc_DOM = new PayloadDOM(properties.getProperty(BOMMAT04_IDoc_File));
        this.DELVRY07_IDoc_DOM = new PayloadDOM(properties.getProperty(DELVRY07_IDoc_File));
        this.MATMAS05_IDoc_DOM = new PayloadDOM(properties.getProperty(MATMAS05_IDoc_File));
        this.ZDELVRY03_IDoc_DOM = new PayloadDOM(properties.getProperty(ZDELVRY03_IDoc_File));

    }

    @After
    public void runAfter() {
        this.BOMMAT04_IDoc_DOM = null;
        this.DELVRY07_IDoc_DOM = null;
        this.MATMAS05_IDoc_DOM = null;
        this.ZDELVRY03_IDoc_DOM = null;
    }

    @Test
    public void getDOCNUM() throws Exception {

        assertEquals("0000000009290233", this.BOMMAT04_IDoc_DOM.getDOCNUM());
        assertEquals("0000000009287919", this.DELVRY07_IDoc_DOM.getDOCNUM());
        assertEquals("0000000009289945", this.MATMAS05_IDoc_DOM.getDOCNUM());
        assertEquals("0000000009287822", this.ZDELVRY03_IDoc_DOM.getDOCNUM());

    }

    @Test
    public void setDOCNUM() throws Exception {

        String docNum = "0000000000000001";

        this.BOMMAT04_IDoc_DOM.setDOCNUM(docNum);
        this.DELVRY07_IDoc_DOM.setDOCNUM(docNum);
        this.MATMAS05_IDoc_DOM.setDOCNUM(docNum);
        this.ZDELVRY03_IDoc_DOM.setDOCNUM(docNum);

        assertEquals(docNum, this.BOMMAT04_IDoc_DOM.getDOCNUM());
        assertEquals(docNum, this.DELVRY07_IDoc_DOM.getDOCNUM());
        assertEquals(docNum, this.MATMAS05_IDoc_DOM.getDOCNUM());
        assertEquals(docNum, this.ZDELVRY03_IDoc_DOM.getDOCNUM());

    }

    @Test
    public void toXML() throws Exception {
        System.out.println(this.BOMMAT04_IDoc_DOM.toXML());
    }

}