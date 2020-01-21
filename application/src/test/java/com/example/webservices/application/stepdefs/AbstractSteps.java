package com.example.webservices.application.stepdefs;

import com.example.webservices.application.CucumberTestContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.LocalServerPort;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

/**
 * Class that abstract test context management and REST API invocation.
 * @author https://github.com/bcarun/cucumber-samples/blob/master/crud-using-datatable/src/test/java/org/arun/cucumber/crudusingdatatable/bdd/stepdefs/AbstractSteps.java
 */
public class AbstractSteps {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSteps.class);

    private CucumberTestContext CONTEXT = CucumberTestContext.CONTEXT;

    @LocalServerPort
    private int port;

    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    protected CucumberTestContext testContext() {
        return CONTEXT;
    }

    /**
     * @author s164410
     * @param <T> type
     * @param type class of the object to instantiate
     * @return maps from a http response to an object of the type T
     * */
    protected <T> T getBody(Class<T> type){
        try {

            return new ObjectMapper().readerFor(type).readValue(CONTEXT.getResponse().thenReturn().getBody().asString());
        }
        catch (Exception e){
            if(e instanceof JsonParseException){
                return (T) CONTEXT.getResponse().thenReturn().getBody().asString();
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    protected void executePost(String apiPath) {
        executePost(apiPath, null, null);
    }

    protected void executePost(String apiPath, Map<String, String> pathParams) {
        executePost(apiPath, pathParams, null);
    }

    protected void executePost(String apiPath, Map<String, String> pathParams, Map<String, String> queryParamas) {
        final RequestSpecification request = CONTEXT.getRequest();
        final Object payload = CONTEXT.getPayload();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        setPayload(request, payload);
        setQueryParams(pathParams, request);
        setPathParams(queryParamas, request);

        Response response = request.accept(ContentType.ANY)
                .log()
                .all()
                .post(url);

        logResponse(response);

        CONTEXT.setResponse(response);
    }

    protected void executeMultiPartPost(String apiPath) {
        final RequestSpecification request = CONTEXT.getRequest();
        final Object payload = CONTEXT.getPayload();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        Response response = request.multiPart("fuelTransfer", payload, "application/json")
                .log()
                .all()
                .post(url);

        logResponse(response);
        CONTEXT.setResponse(response);
    }

    protected void executeDelete(String apiPath) {
        executeDelete(apiPath, null, null);
    }

    protected void executeDelete(String apiPath, Map<String, String> pathParams) {
        executeDelete(apiPath, pathParams, null);
    }

    protected void executeDelete(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = CONTEXT.getRequest();
        final Object payload = CONTEXT.getPayload();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        setPayload(request, payload);
        setQueryParams(pathParams, request);
        setPathParams(queryParams, request);

        Response response = request.accept(ContentType.ANY)
                .log()
                .all()
                .delete(url);

        logResponse(response);
        CONTEXT.setResponse(response);
    }

    protected void executePut(String apiPath) {
        executePut(apiPath, null, null);
    }

    protected void executePut(String apiPath, Map<String, String> pathParams) {
        executePut(apiPath, pathParams, null);
    }

    protected void executePut(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = CONTEXT.getRequest();
        final Object payload = CONTEXT.getPayload();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        setPayload(request, payload);
        setQueryParams(pathParams, request);
        setPathParams(queryParams, request);

        Response response = request.accept(ContentType.JSON)
                .log()
                .all()
                .put(url);

        logResponse(response);
        CONTEXT.setResponse(response);
    }

    protected void executePatch(String apiPath) {
        executePatch(apiPath, null, null);
    }

    protected void executePatch(String apiPath, Map<String, String> pathParams) {
        executePatch(apiPath, pathParams, null);
    }

    protected void executePatch(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = CONTEXT.getRequest();
        final Object payload = CONTEXT.getPayload();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        setPayload(request, payload);
        setQueryParams(pathParams, request);
        setPathParams(queryParams, request);

        Response response = request.accept(ContentType.JSON)
                .log()
                .all()
                .patch(url);

        logResponse(response);
        CONTEXT.setResponse(response);
    }

    protected void executeGet(String apiPath) {
        executeGet(apiPath, null);
    }

    protected void executeGet(String apiPath, Map<String, String> pathParams) {
        executeGet(apiPath, pathParams, null);
    }

    protected void executeGet(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = CONTEXT.getRequest();
        String url = baseUrl() + apiPath;
        if(apiPath.startsWith("http")){
            url = apiPath;
        }

        setQueryParams(pathParams, request);
        setPathParams(queryParams, request);

        Response response = request.accept(ContentType.JSON)
                .log()
                .all()
                .get(url);

        logResponse(response);
        CONTEXT.setResponse(response);
    }

    private void logResponse(Response response) {
        response.then()
                .log()
                .all();
    }

    private void setPathParams(Map<String, String> queryParamas, RequestSpecification request) {
        if (null != queryParamas) {
            request.queryParams(queryParamas);
        }
    }

    private void setQueryParams(Map<String, String> pathParams, RequestSpecification request) {
        if (null != pathParams) {
            request.pathParams(pathParams);
        }
    }

    private void setPayload(RequestSpecification request, Object payload) {
        if (null != payload) {
            request.contentType(ContentType.JSON)
                    .body(payload);
        }
    }

}