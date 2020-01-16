package com.example.webservices.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gherkin.deps.com.google.gson.Gson;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8080")
@ContextConfiguration
public class SpringIntegrationTest {
    protected static ResponseResults latestResponse = null;

    @Autowired
    protected RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    protected void executeGet(String url) throws IOException {
        execute(HttpMethod.GET, url,null);
    }

    protected void executeDelete(String resource){
        execute(HttpMethod.DELETE, resource, null);
    }

    protected void executePost(String resource, Object body) {
        execute(HttpMethod.POST, resource, body);
    }
    private void execute(HttpMethod method, String resource, Object body){
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if(method != HttpMethod.GET){
            if (restTemplate == null) {
                restTemplate = new RestTemplate();
            }
            if(body != null){
                String jsonBody = new Gson().toJson(body);
                requestCallback.setBody(jsonBody);
            }
        }
        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(baseUrl()+resource, method,requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return (new ResponseResults(response));
                    }
                });
    }
    protected void executePut(String resource, Object body) {
        execute(HttpMethod.PUT, resource, body);
    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }
}