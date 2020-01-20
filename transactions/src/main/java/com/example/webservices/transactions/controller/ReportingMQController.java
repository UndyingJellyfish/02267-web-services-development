package com.example.webservices.transactions.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IReportingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportingMQController extends RabbitHelper {

    private final IReportingService reportingService;

    public ReportingMQController(IReportingService reportingService){
        this.reportingService = reportingService;
    }


    @RabbitListener(queues = QUEUE_REPORTING_HISTORY)
    public ResponseObject getReportingHistory(UUID accountId) {

        try {

            List<TransactionDto> transactionDtos = this.reportingService.getTransactionHistory(accountId);

            return success(transactionDtos);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    // TODO reporting history with date
}
