package com.coherent.api.training;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {

    private final TokenManager tokenManager;

    public MyHttpClient() {
        tokenManager = TokenManager.getInstance();
    }

    public String executeRequest(HttpRequestBase request) {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            String token = null;
            if (request instanceof HttpPost || request instanceof HttpPut || request instanceof HttpPatch || request instanceof HttpDelete) {
                token = tokenManager.getWriteToken();
            } else if (request instanceof HttpGet) {
                token = tokenManager.getReadToken();
            }

            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("content-type", "application/json");
            request.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
