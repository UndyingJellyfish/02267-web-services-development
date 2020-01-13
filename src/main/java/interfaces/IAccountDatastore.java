package interfaces;

import models.Account;
import models.Customer;
import models.Merchant;

import java.util.UUID;

public interface IAccountDatastore {
    Customer getCustomer(UUID customerId);
    <T extends Account> T addAccount(T account);

    Merchant getMerchant(UUID merchantId);
}
