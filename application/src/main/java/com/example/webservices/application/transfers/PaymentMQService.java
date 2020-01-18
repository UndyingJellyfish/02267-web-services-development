package com.example.webservices.application.transfers;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.ResponseObject;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.IPaymentService;
import com.example.webservices.library.interfaces.IReportingService;
import gherkin.deps.com.google.gson.reflect.TypeToken;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentMQService extends RabbitMQBaseClass implements IPaymentService {

    public PaymentMQService(RabbitTemplate rabbitTemplate, @Qualifier("payments") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, String description) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException {
        return null;
    }

    @Override
    public TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException {
        return null;
    }

    @Override
    public void refund(UUID customerId, UUID merchantId, UUID tokenId) throws TokenException, BankException, EntryNotFoundException, InvalidTransferAmountException {

    }
}
