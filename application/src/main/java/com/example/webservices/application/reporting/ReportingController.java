package com.example.webservices.application.reporting;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.interfaces.IReportingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reporting")
public class ReportingController {

    private final IReportingService reportingService;

    public ReportingController(IReportingService reportingService){
        this.reportingService = reportingService;
    }

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDto> getHistory(@PathVariable UUID accountId){
        try{
            return reportingService.getTransactionHistory(accountId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{accountId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionDto> getHistorySince(@PathVariable UUID accountId, @PathVariable Date date){
        try{
            return reportingService.getTransactionHistorySince(new RequestReportingHistoryDto(accountId, date));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
