package com.example.webservices.library.dataTransferObjects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author s164395
 * generic object to return data back to a user (i.e a wrapper for a HTTP body)
 */
public class ResponseObject implements Serializable {

    private HttpStatus statusCode;
    private String body;

    private static String toJson(Object body){
        try{
            return new ObjectMapper().writeValueAsString(body);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseObject(HttpStatus statusCode){
        this.statusCode = statusCode;
    }
    public ResponseObject(HttpStatus statusCode, String body){
        this(statusCode);
        this.body = body;
    }


    public ResponseObject(HttpStatus statusCode, Object body){
        this(statusCode, toJson(body));
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
