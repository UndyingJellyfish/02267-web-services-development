package main.controllers;
import main.exceptions.TokenException;
import main.models.Transaction;
import main.reporting.ReportingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reporting")
public class ReportingController {

    private final ReportingService reportingService;


    public ReportingController(ReportingService reportingService){
        this.reportingService = reportingService;
    }


    @GetMapping("/{UUID}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> merchantHistory(@PathVariable UUID accountId){
        try{
            return reportingService.getTransactionHistory(accountId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
