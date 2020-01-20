package com.example.webservices.library.dataTransferObjects;

import java.util.Date;
import java.util.UUID;

public class RequestReportingHistoryDto {
    UUID accountId;
    Date startDate;

    public RequestReportingHistoryDto(UUID id) {
        this(id, null);
    }
    public RequestReportingHistoryDto(UUID id, Date startDate) {
        this.accountId = id;
        this.startDate = startDate;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}