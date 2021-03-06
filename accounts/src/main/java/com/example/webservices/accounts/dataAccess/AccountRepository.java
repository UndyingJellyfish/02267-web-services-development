package com.example.webservices.accounts.dataAccess;

import com.example.webservices.accounts.models.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/** @author s164398 */
public interface AccountRepository<T extends Account> extends CrudRepository<T, UUID> { }

