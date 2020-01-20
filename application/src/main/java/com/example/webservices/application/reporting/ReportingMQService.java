package com.example.webservices.application.reporting;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IReportingService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportingMQService extends RabbitMQBaseClass implements IReportingService {
    public ReportingMQService(RabbitTemplate rabbitTemplate, @Qualifier("reporting") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_REPORTING_HISTORY, id);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new EntryNotFoundException();
        }

        return Arrays.asList(fromJson(response.getBody(), TransactionDto[].class));
    }

    @Override
    public List<TransactionDto> getTransactionHistorySince(RequestReportingHistoryDto dto) throws EntryNotFoundException {
        ResponseObject response = send(QUEUE_REPORTING_HISTORY, dto);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new EntryNotFoundException();
        }

        return Arrays.asList(fromJson(response.getBody(), TransactionDto[].class));
    }
}
