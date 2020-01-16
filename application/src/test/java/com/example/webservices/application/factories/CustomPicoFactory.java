/*
package com.example.webservices.application.factories;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import com.example.webservices.application.dataAccess.InMemoryDatastore;
import com.example.webservices.application.reporting.ReportingService;
import com.example.webservices.application.tokens.TokenManager;
import com.example.webservices.application.accounts.AccountService;
import com.example.webservices.library.models.Transaction;

public class CustomPicoFactory implements ObjectFactory {

    private final PicoFactory delegate = new PicoFactory();

    public CustomPicoFactory(){

        delegate.addClass(InMemoryDatastore.class);
        delegate.addClass(TokenManager.class);
        delegate.addClass(Transaction.class);
        delegate.addClass(AccountService.class);
        delegate.addClass(ReportingService.class);
    }


    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return delegate.addClass(glueClass);
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        return delegate.getInstance(glueClass);
    }
}
*/
