package com.example.webservices.application.reporting;
import com.example.webservices.library.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reporting")
public class ReportingController {

    private final ReportingService reportingService;


    public ReportingController(ReportingService reportingService){
        this.reportingService = reportingService;
    }


    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getHistory(@PathVariable UUID accountId){
        try{
            return reportingService.getTransactionHistory(accountId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}