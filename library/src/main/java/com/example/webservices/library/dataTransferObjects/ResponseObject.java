package com.example.webservices.library.dataTransferObjects;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseObject implements Serializable {

    private HttpStatus statusCode;
    private String body;

    public ResponseObject(HttpStatus statusCode){
        this.statusCode = statusCode;
    }
    public ResponseObject(HttpStatus statusCode, String body){
        this(statusCode);
        this.body = body;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
