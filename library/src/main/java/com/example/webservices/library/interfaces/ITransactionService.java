package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {

    /**
     * @param id the id of the account
     * @return yields the transactions stored associated to the account id given
     */
    List<TransactionDto> getTransactions(UUID id);

    /**
     * @param tokenId the id of the token to refund
     * @return refunds the money transferred when that token was used and returns the ID of the new refund transaction
     * @throws EntryNotFoundException when no transaction or token is found
     * @throws DuplicateEntryException when the refund has already taken place
     */
    UUID refundTransaction(UUID tokenId) throws EntryNotFoundException, DuplicateEntryException;

    /**
     * @param transaction persists a new transaction
     * @return the id of the newly added transaction
     * @throws DuplicateEntryException thrown when the transaction already exists
     */
    UUID addTransaction(TransactionDto transaction) throws DuplicateEntryException;

    /**
     * @param tokenId the token to search for
     * @return gets the transaction related to a token
     * @throws EntryNotFoundException thrown when the token has not been used or does not exist
     */
    TransactionDto getTransaction(UUID tokenId) throws EntryNotFoundException;
}
