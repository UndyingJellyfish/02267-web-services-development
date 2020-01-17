/*
package com.example.webservices.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;

import gherkin.deps.com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseResults {
    private final ClientHttpResponse theResponse;
    private final String body;

    ResponseResults(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();
        final StringWriter stringWriter = new StringWriter();
        IOUtils.copy(bodyInputStream, stringWriter);
        this.body = stringWriter.toString();
    }

    public ClientHttpResponse getTheResponse() {
        return theResponse;
    }

    public <T> T getBody(Class<T> type) {
        return new Gson().fromJson(body, type);
    }
    public <T> T getBody(Type type){
        return new Gson().fromJson(body, type);
    }
}*/