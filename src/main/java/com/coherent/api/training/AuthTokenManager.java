package com.coherent.api.training;

import com.coherent.api.training.config.ConfigReader;
import com.coherent.api.training.enums.Scope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class AuthTokenManager {
    private static AuthTokenManager INSTANCE;
    private static final String USERNAME = ConfigReader.getInstance().getProperty("username");
    private static final String PASSWORD = ConfigReader.getInstance().getProperty("password");
    private static final String BASE_URL = ConfigReader.getInstance().getProperty("base_url");
    private static final String OAUTH_ENDPOINT = ConfigReader.getInstance().getProperty("oauth_endpoint");

    private AuthTokenManager() {
    }

    public static synchronized AuthTokenManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthTokenManager();
        }
        return INSTANCE;
    }

    public String getReadToken() {
        return getAuthToken(Scope.READ);
    }

    public String getWriteToken() {
        return getAuthToken(Scope.WRITE);
    }

    private String getAuthToken(Scope scope) {
        HttpResponse response = Request.postRequest(BASE_URL + OAUTH_ENDPOINT)
                .setHeader("Content-Type", "application/json")
                .setParameter("grant_type", "client_credentials")
                .setParameter("scope", scope.getScope())
                .setBasicAuthentication(USERNAME, PASSWORD)
                .executeRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getEntity().getContent(), AuthTokenResponse.class).getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException("Access toke was not returned!");
        }

    }
}

