package com.coherent.api.training;

import org.apache.http.client.methods.HttpGet;

public class ApiTests {


    // ToDo: Check was added temporary to execute GET request with read scope, remove
    public static void main(String[] args) {
        MyHttpClient httpClient = new MyHttpClient();
        HttpGet getRequest = new HttpGet("http://localhost:8888/swagger-ui/");
        String result = httpClient.executeRequest(getRequest);
        System.out.println("GET response: " + result);
    }
}
