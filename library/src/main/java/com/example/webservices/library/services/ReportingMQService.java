package com.example.webservices.library.services;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IReportingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ReportingMQService extends RabbitMQBaseClass implements IReportingService {
    public ReportingMQService(RabbitTemplate rabbitTemplate, @Qualifier("reporting") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    /**
     * @author s164395
     * @param id account of the user to search for
     * @return
     * @throws EntryNotFoundException
     */
    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_REPORTING_HISTORY, id);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(),fromJson(response.getBody(), String.class));
        }

        return Arrays.asList(fromJson(response.getBody(), TransactionDto[].class));
    }

    /**
     * @author s164395
     * @param dto data object containing the id of the user and the search date
     * @return
     * @throws EntryNotFoundException
     */
    @Override
    public List<TransactionDto> getTransactionHistorySince(RequestReportingHistoryDto dto) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_REPORTING_HISTORY_DATE, dto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ResponseStatusException(response.getStatusCode(),fromJson(response.getBody(), String.class));
        }

        return Arrays.asList(fromJson(response.getBody(), TransactionDto[].class));
    }
}
