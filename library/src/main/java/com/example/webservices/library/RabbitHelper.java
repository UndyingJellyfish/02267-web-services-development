package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import gherkin.deps.com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;

public abstract class RabbitHelper {

    public static final String QUEUE_ACCOUNT_GET = "account.get";
    public static final String QUEUE_CUSTOMER_SIGNUP = "account.signup.customer";
    public static final String QUEUE_MERCHANT_SIGNUP = "account.signup.merchant";
    public static final String QUEUE_ACCOUNT_CHANGENAME = "account.changename";
    public static final String QUEUE_ACCOUNT_DELETE = "account.delete";

    public static final String QUEUE_TOKENS_GET = "tokens.get";
    public static final String QUEUE_TOKENS_REQUEST = "tokens.request";
    public static final String QUEUE_TOKENS_RETIRE = "tokens.retire";


    protected Gson gson = new Gson();

    protected  <T> T fromJson(String response, Class<T> type){
        return gson.fromJson(response, type);
    }
    protected  <T> T fromJson(String response, Type type){
        return gson.fromJson(response, type);
    }
    protected <T> String toJson(T message){
        return gson.toJson(message);
    }

    protected String success(){
        return toJson(new ResponseObject(HttpStatus.OK));
    }

    protected <T> String success(T response){
        return toJson(new ResponseObject(HttpStatus.OK, toJson(response)));
    }
    protected <T> String failure(T response, HttpStatus status){
        return toJson(new ResponseObject(status, toJson(response)));
    }
    protected <T> String failure(T response){
        return failure(response, HttpStatus.BAD_REQUEST);
    }

}
