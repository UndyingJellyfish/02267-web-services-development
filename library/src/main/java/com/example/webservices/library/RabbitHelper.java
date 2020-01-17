package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import gherkin.deps.com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;

public class RabbitHelper {

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

    protected <T> String success(T response){
        return toJson(new ResponseObject(HttpStatus.OK, toJson(response)));
    }
    protected <T> String failure(T response){
        return toJson(new ResponseObject(HttpStatus.BAD_REQUEST, toJson(response)));
    }

}
