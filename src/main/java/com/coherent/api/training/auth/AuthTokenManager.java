package com.coherent.api.training.auth;

import com.coherent.api.training.client.CustomHttpClient;
import com.coherent.api.training.config.ConfigReader;
import com.coherent.api.training.model.AuthTokenResponse;
import com.coherent.api.training.model.Scope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class AuthTokenManager {
    private static AuthTokenManager INSTANCE;
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
        HttpResponse response = CustomHttpClient.executePostWithBasicAuthAndScope(OAUTH_ENDPOINT,scope);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getEntity().getContent(), AuthTokenResponse.class).getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException("Access token was not returned!");
        }
    }
}

