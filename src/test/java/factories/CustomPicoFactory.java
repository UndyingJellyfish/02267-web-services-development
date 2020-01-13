package factories;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import main.InMemoryDatastore;
import main.TokenManager;
import main.AccountService;
import models.Transaction;

public class CustomPicoFactory implements ObjectFactory {

    private final PicoFactory delegate = new PicoFactory();

    public CustomPicoFactory(){

        delegate.addClass(InMemoryDatastore.class);
        delegate.addClass(TokenManager.class);
        delegate.addClass(Transaction.class);
        delegate.addClass(AccountService.class);
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
