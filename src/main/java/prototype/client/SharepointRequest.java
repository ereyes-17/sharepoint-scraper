package prototype.client;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;

import prototype.config.SharepointConfig;

public class SharepointRequest {
    private final SharepointConfig sharepointConfig;

    public SharepointRequest(SharepointConfig sharepointConfig) {
        this.sharepointConfig = sharepointConfig;
    }

    public HttpGet createGetRequestObject(String targetPath) {
        HttpGet httpGet = new HttpGet(targetPath);
        /* header for getting response in json */
        httpGet.addHeader(HttpHeaders.ACCEPT, sharepointConfig.getODATA_PARAM());
        /* set a custom header (for EOP) */
        httpGet.addHeader("X-FORMS_BASED_AUTH_ACCEPTED", "f");
        System.out.println(" --> " + targetPath);
        return httpGet;
    }
}
