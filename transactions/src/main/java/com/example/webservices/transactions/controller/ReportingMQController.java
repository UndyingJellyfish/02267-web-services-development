package com.example.webservices.transactions.controller;

import com.example.webservices.library.RabbitHelper;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IReportingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
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
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RabbitListener(queues = QUEUE_REPORTING_HISTORY_DATE)
    public ResponseObject getReportingHistory(RequestReportingHistoryDto dto) {
        try {
            List<TransactionDto> transactionDtos = this.reportingService.getTransactionHistorySince(dto);
            return success(transactionDtos);
        } catch (EntryNotFoundException e) {
            return failure(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
