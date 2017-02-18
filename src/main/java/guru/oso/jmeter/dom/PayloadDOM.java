package guru.oso.jmeter.dom;

import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by BC on 12/25/16.
 */
public class PayloadDOM {

    private static final Logger logger = LoggerFactory.getLogger(PayloadDOM.class);

    private org.jdom2.Document payloadDOM;

    public PayloadDOM(final String idocPath) {

        this.payloadDOM = this.readIDocIntoJDOM(idocPath);

    }

    private org.jdom2.Document readIDocIntoJDOM(final String idocPath) {

        org.jdom2.Document payloadDoc;

        DocumentBuilder dBuilder = this.generateDocumentBuilder();

        org.w3c.dom.Document doc;
        if (dBuilder != null) {
            File idocFile = new File(idocPath);
            doc = this.parseIDoc(dBuilder, idocFile);
        } else {
            doc = null;
        }

        if (doc != null) {
            DOMBuilder domBuilder = new DOMBuilder();
            payloadDoc = domBuilder.build(doc);
        } else {
            payloadDoc = null;
        }

        return payloadDoc;

    }

    private DocumentBuilder generateDocumentBuilder() {

        DocumentBuilder builder;

        //creating DOM Document
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            logger.error("Document Builder error.", pce);
            builder = null;
        }

        return builder;

    }

    private org.w3c.dom.Document parseIDoc(final DocumentBuilder dBuilder, final File idocFile) {

        org.w3c.dom.Document doc;

        try {
            doc = dBuilder.parse(idocFile);
        } catch (SAXException se) {
            logger.error("Error parsing file.", se);
            doc = null;
        } catch (IOException ioe) {
            logger.error("Unable to parse file.", ioe);
            doc = null;
        }

        return doc;

    }

    public String toXML() {
        return new XMLOutputter().outputString(payloadDOM);
    }

    public String getDOCNUM() {

        Element docNum = payloadDOM.getRootElement().getChild("IDOC").getChild("EDI_DC40").getChild("DOCNUM");

        return docNum.getText();

    }

    public void setDOCNUM(final String docNum) {

        logger.info("Document Number: " + docNum);

        Element rootElement = payloadDOM.getRootElement();
        Element idocElement = rootElement.getChild("IDOC");
        Element ediElement = idocElement.getChild("EDI_DC40");
        Element docNumElement = ediElement.getChild("DOCNUM");
        docNumElement.setText(docNum);

    }

}
