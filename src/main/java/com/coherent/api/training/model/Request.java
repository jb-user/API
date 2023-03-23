package com.coherent.api.training.model;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static com.coherent.api.training.model.HttpMethod.*;

public class Request {
    private final HttpRequest request;
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private Request(String uri, HttpMethod method) {
        switch (method) {
            case GET:
                request = new HttpGet(uri);
                break;
            case POST:
                request = new HttpPost(uri);
                break;
            case PUT:
                request = new HttpPut(uri);
                break;
            case PATCH:
                request = new HttpPatch(uri);
                break;
            case DELETE:
                request = new HttpDelete(uri);
                break;
            default:
                throw new RuntimeException("Unknown request method!");
        }
    }

    public static Request getRequest(String uri) {
        return new Request(uri, GET);
    }

    public static Request postRequest(String uri) {
        return new Request(uri, POST);
    }

    public static Request putRequest(String uri) {
        return new Request(uri, PUT);
    }

    public static Request patchRequest(String uri) {
        return new Request(uri, PATCH);
    }

    public static Request deleteRequest(String uri) {
        return new Request(uri, DELETE);
    }

    public Request setHeader(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public Request setHeaders(Map<String,String> headers) {
        headers.forEach((key, value) -> request.setHeader(key, value));
        return this;
    }

    public Request setParameter(String key, String value) {
        try {
            URI uri = new URIBuilder(request.getRequestLine().getUri()).addParameter(key, value).build();
            ((HttpRequestBase) request).setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Request setBasicAuthentication(String username, String password) {
        String authPair = username + ":" + password;
        String encodedAuthPair = Base64.encodeBase64String(authPair.getBytes());
        setHeader("Authorization", "Basic " + encodedAuthPair);
        return this;
    }

    public Request setBearerToken(String token) {
        setHeader("Authorization", "Bearer " + token);
        return this;
    }

    public Request setBody(String requestBody) {
        try {
            StringEntity entity = new StringEntity(requestBody);
            ((HttpPost) request).setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public HttpResponse executeRequest() {
        try {
            return httpClient.execute((HttpUriRequest) request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
