package com.coherent.api.training;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TokenManager {

    private static TokenManager instance;
    private static String writeToken;
    private static String readToken;

    private TokenManager() {
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
            instance.getWriteToken();
            instance.getReadToken();
        }
        return instance;
    }

    public String getWriteToken() {
        if (writeToken == null) {
            writeToken = getToken("write");
        }
        return writeToken;
    }

    public String getReadToken() {
        if (readToken == null) {
            readToken = getToken("read");
        }
        return readToken;
    }

    private String getToken(String scope) {
        String token = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost("http://localhost:8888/oauth/token");
            String authString = "0oa157tvtugfFXEhU4x7:X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";
            StringEntity params = new StringEntity("grant_type=client_credentials&scope=" + scope);
            request.addHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(authString.getBytes()));
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponse tokenResponse = objectMapper.readValue(responseString, TokenResponse.class);
            token = tokenResponse.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}

