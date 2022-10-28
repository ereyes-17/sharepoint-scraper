package prototype.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SharepointResponseExceptionHandler {

    public void handleResponseFromStatusCode(int statusCode) {
        if (statusCode != 200) {
            System.out.println("ERROR FROM SHAREPOINT API!");
            throw new RuntimeException("Status code returned is " + statusCode);
        }
    }

    public void handleResponseFromStatusCode(int statusCode, HttpResponse response) throws IOException {
        if (statusCode != 200) {
            System.out.println("ERROR FROM SHAREPOINT API!");
            System.out.println(EntityUtils.toString(response.getEntity()));

            throw new RuntimeException("Status code returned is " + statusCode);
        }
    }
}
