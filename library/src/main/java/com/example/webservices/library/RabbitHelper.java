package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public abstract class RabbitHelper {

    public static final String QUEUE_ACCOUNT_GET = "account.get";
    public static final String QUEUE_CUSTOMER_SIGNUP = "account.signup.customer";
    public static final String QUEUE_MERCHANT_SIGNUP = "account.signup.merchant";
    public static final String QUEUE_ACCOUNT_CHANGENAME = "account.changename";
    public static final String QUEUE_ACCOUNT_DELETE = "account.delete";

    public static final String QUEUE_TOKENS_GET = "tokens.get";
    public static final String QUEUE_TOKENS_REQUEST = "tokens.request";
    public static final String QUEUE_TOKENS_RETIRE = "tokens.retire";
    public static final String QUEUE_TOKEN_GET = "token.get";
    public static final String QUEUE_TOKEN_USE = "token.use";
    public static final String QUEUE_TOKEN_REQUEST = "token.request";

    public static final String QUEUE_REPORTING_HISTORY = "reporting.history";
    public static final String QUEUE_TRANSACTION_ADD = "transaction.add";
    public static final String QUEUE_TRANSACTION_GET = "transaction.get";

    public static final String QUEUE_PAYMENT_TRANSFER = "payment.transfer";
    public static final String QUEUE_PAYMENT_REFUND = "payment.refund";

    protected ObjectMapper jackson = new ObjectMapper();

    protected  <T> T fromJson(String response, Class<T> type) {
        try{
            return jackson.readerFor(type).readValue(response);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    protected <T> String toJson(T message) {
        try{
            return jackson.writeValueAsString(message);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    protected <T> ResponseObject success(T response) throws JsonProcessingException {
        return new ResponseObject(HttpStatus.OK, toJson(response));
    }

    protected ResponseObject success() throws JsonProcessingException {
        return success("success");
    }

    protected <T> ResponseObject failure(T response, HttpStatus status) throws JsonProcessingException {
        return new ResponseObject(status, toJson(response));
    }
    protected <T> ResponseObject failure(T response) throws JsonProcessingException {
        return failure(response, HttpStatus.BAD_REQUEST);
    }

}
