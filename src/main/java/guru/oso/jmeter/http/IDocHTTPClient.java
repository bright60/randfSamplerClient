package guru.oso.jmeter.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by BC on 12/23/16.
 */
public class IDocHTTPClient {

    private static final Logger logger = LoggerFactory.getLogger(IDocHTTPClient.class);

    public static HttpUriRequest generatePostRequest(final String idocXML, final String url) {

        HttpUriRequest request;

        HttpEntity entity = readIDOCtoEntity(idocXML);

        if (entity != null) {
            request = RequestBuilder.post().setEntity(entity).setUri(url).build();
        } else {
            request = null;
        }

        return request;

    }

    public static String readIDOCtoString(final String idoc_path_string) {

        String idocXML;

        try {

            idocXML = new String(Files.readAllBytes(Paths.get(idoc_path_string)));

        } catch (IOException ioe) {
            ioe.printStackTrace();
            idocXML = null;
        }

        return idocXML;

    }


    private static HttpEntity readIDOCtoEntity(final String idocXML) {

        HttpEntity entity = null;

        try {

            entity = new ByteArrayEntity(idocXML.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return entity;

    }

}
