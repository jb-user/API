package com.coherent.api.training.client;

import com.coherent.api.training.auth.AuthTokenManager;
import com.coherent.api.training.config.ConfigReader;
import com.coherent.api.training.enums.Scope;
import com.coherent.api.training.model.Request;
import org.apache.http.HttpResponse;

import java.util.Map;

public class CustomHttpClient {

    private static final String USERNAME = ConfigReader.getInstance().getProperty("username");
    private static final String PASSWORD = ConfigReader.getInstance().getProperty("password");
    public static final String BASE_URL = ConfigReader.getInstance().getProperty("base_url");

    public static HttpResponse executeGet(String endpoint) {
        return Request.getRequest(BASE_URL + endpoint)
                .setBearerToken(AuthTokenManager.getInstance().getReadToken())
                .executeRequest();
    }

    public static HttpResponse executePost(String endpoint, String requestBody) {
        return executePost(endpoint, requestBody, null);
    }

    public static HttpResponse executePost(String endpoint, String requestBody, String authToken) {
        Request request = Request.postRequest(BASE_URL + endpoint)
                .setHeader("Content-Type", "application/json");

        if (authToken != null) {
            request.setBearerToken(authToken);
        } else {
            request.setBearerToken(AuthTokenManager.getInstance().getWriteToken());
        }

        if (requestBody != null) {
            request.setBody(requestBody);
        }

        return request.executeRequest();
    }

    public static HttpResponse executePostWithBasicAuth(String endpoint) {
        return executePostWithBasicAuth(endpoint, null);
    }

    public static HttpResponse executePostWithBasicAuth(String endpoint, String requestBody) {
        Request request = Request.postRequest(BASE_URL + endpoint)
                .setBasicAuthentication(USERNAME, PASSWORD)
                .setHeader("Content-Type", "application/json");

        if (requestBody != null) {
            request.setBody(requestBody);
        }

        return request.executeRequest();
    }

    public static HttpResponse executePostWithBasicAuthAndScope(String endpoint, Scope scope) {
        Request request = Request.postRequest(BASE_URL + endpoint)
                .setHeader("Content-Type", "application/json")
                .setParameter("grant_type", "client_credentials")
                .setParameter("scope", scope.getScope())
                .setBasicAuthentication(USERNAME, PASSWORD);
        return request.executeRequest();
    }

    public static HttpResponse executePostWithHeadersSetUp(String endpoint, Map<String, String> headers, String requestBody, String username, String password) {
        Request request = Request.postRequest(BASE_URL + endpoint)
                .setBasicAuthentication(username, password);
        if (headers != null) {
            request.setHeaders(headers);
        }
        if (requestBody != null) {
            request.setBody(requestBody);
        }
        return request.executeRequest();
    }
}