package com.example.webservices.library;

import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public abstract class RabbitHelper {

    public static final String QUEUE_ACCOUNT_GET = "account.get";
    public static final String QUEUE_CUSTOMER_SIGNUP = "account.signup.customer";
    public static final String QUEUE_MERCHANT_SIGNUP = "account.signup.merchant";
    public static final String QUEUE_ACCOUNT_CHANGENAME = "account.changename";
    public static final String QUEUE_ACCOUNT_DELETE = "account.delete";

    public static final String QUEUE_TOKENS_GET = "tokens.get";
    public static final String QUEUE_TOKENS_REQUEST = "tokens.request";
    public static final String QUEUE_TOKENS_RETIRE = "tokens.retire";
    public static final String QUEUE_TOKENS_ACTIVE = "tokens.get.active";
    public static final String QUEUE_TOKEN_GET = "token.get";
    public static final String QUEUE_TOKEN_USE = "token.use";
    public static final String QUEUE_TOKEN_REQUEST = "token.request";

    public static final String QUEUE_REPORTING_HISTORY = "reporting.history";
    public static final String QUEUE_REPORTING_HISTORY_DATE = "reporting.history.date";
    public static final String QUEUE_TRANSACTION_ADD = "transaction.add";
    public static final String QUEUE_TRANSACTION_REFUND = "transaction.refund";
    public static final String QUEUE_TRANSACTION_GET = "transaction.get";

    public static final String QUEUE_PAYMENT_TRANSFER = "payment.transfer";
    public static final String QUEUE_PAYMENT_REFUND = "payment.refund";


    protected ObjectMapper jackson = new ObjectMapper();

    /**
     * @author s164424
     * @param response A json response
     * @param type The type to deserialize to
     * @param <T> The type to deserialize to
     * @return The deserialized response
     */
    protected  <T> T fromJson(String response, Class<T> type) {
        try{
            return jackson.readerFor(type).readValue(response);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @author s164424
     * @param message A message to be serialized to json
     * @param <T> The type of the message
     * @return The message deserialized to a json string
     */
    protected <T> String toJson(T message) {
        try{
            return jackson.writeValueAsString(message);
        }
        catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @author s164424
     * @param <T> type of the response object
     * @param response the response to package
     * @return a generic response
     */
    protected <T> ResponseObject success(T response) {
        return new ResponseObject(HttpStatus.OK, toJson(response));
    }

    /**
     * @author s164424
     * @return a basic response object
     */
    protected ResponseObject success() {
        return success("success");
    }


    /**
     * @author s164424
     * @param e cause of the internal server error
     * @return a response object with an error
     */
    protected ResponseObject error(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(e instanceof ResponseStatusException){
            status = ((ResponseStatusException) e).getStatus();
        }
        return new ResponseObject(status, toJson(e.getMessage()));
    }

    /**
     * @author s164424
     * @param <T> type of a generic response object
     * @param response return body
     * @param status status to wrap into the response
     * @return a response object with an error
     */
    protected <T> ResponseObject failure(T response, HttpStatus status) {
        return new ResponseObject(status, toJson(response));
    }
    /**
     * @author s164424
     * @param <T> type of a generic response object
     * @param response a response object with an error
     * @return The response object with http status 400
     */
    protected <T> ResponseObject failure(T response) {
        return failure(response, HttpStatus.BAD_REQUEST);
    }

}
