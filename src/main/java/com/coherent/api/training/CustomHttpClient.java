
package com.coherent.api.training;

import com.coherent.api.training.config.ConfigReader;
import org.apache.http.HttpResponse;

public class CustomHttpClient {

    public static final  String BASE_URL = ConfigReader.getInstance().getProperty("base_url");

    public static HttpResponse executeGet(String endpoint) {
        return Request.getRequest(BASE_URL + endpoint)
                .setBearerToken(AuthTokenManager.getInstance().getReadToken())
                .executeRequest();
    }

    public static HttpResponse executePost(String endpoint) {
        return Request.postRequest(BASE_URL + endpoint)
                .setBearerToken(AuthTokenManager.getInstance().getWriteToken())
                .setHeader("Content-Type", "application/json")
                .executeRequest();
    }
}
