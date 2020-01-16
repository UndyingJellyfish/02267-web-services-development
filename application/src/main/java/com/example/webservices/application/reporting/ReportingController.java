package com.example.webservices.application.reporting;
import com.example.webservices.application.transfers.TransactionDto;
import com.example.webservices.application.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reporting")
public class ReportingController {

    private final ReportingService reportingService;


    public ReportingController(ReportingService reportingService){
        this.reportingService = reportingService;
    }


    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDto> getHistory(@PathVariable UUID accountId){
        try{
            List<Transaction> transactions = reportingService.getTransactionHistory(accountId);
            return transactions.stream().map(TransactionDto::new).collect(Collectors.toList());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
