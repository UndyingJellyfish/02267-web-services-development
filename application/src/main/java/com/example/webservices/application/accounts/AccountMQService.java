package com.example.webservices.application.accounts;

import com.example.webservices.library.RabbitMQBaseClass;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountMQService extends RabbitMQBaseClass implements IAccountService {

    private static final String QUEUE = "account.get";

    public AccountMQService(RabbitTemplate rabbitTemplate, @Qualifier("accounts") DirectExchange exchange) {
        super(rabbitTemplate, exchange);
    }

    @Override
    public AccountDto getCustomer(UUID customerId) throws EntryNotFoundException {
        return getAccount(customerId);
    }

    @Override
    public AccountDto getAccount(UUID id) throws EntryNotFoundException {
        return fromJson(send(QUEUE, id), AccountDto.class);
    }

    @Override
    public AccountDto getMerchant(UUID merchantId) throws EntryNotFoundException {
        return getAccount(merchantId);
    }
}
