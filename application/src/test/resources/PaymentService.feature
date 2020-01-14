Feature: Payment Service Tests

  Background: Payment Service test cases
    Scenario: Creating a transaction
      Given A merchant
      And A valid token
      And A positive amount
      When The merchant initiates the transaction
      Then The transaction should go through

    Scenario: Creating an invalid transaction with a non-existing token
      Given A merchant
      And A token that doesn't exist
      And A positive amount
      When The merchant initiates the invalid transaction
      Then The transaction should fail

    Scenario: Creating an invalid transaction with an used token
      Given A merchant
      And A positive amount
      And A token that has already been used
      When The merchant initiates the invalid transaction
      Then The transaction should fail and inform that the token is used

  Scenario Outline: Creating an invalid transaction with an invalid amount
     Given A merchant
     And An invalid <amount>
     And A valid token
     When The merchant initiates the invalid transaction
     Then The transaction should fail and inform that the amount is invalid
    Examples:
      | amount |
      | 0      |
      |    -23 |


    Scenario: Refunding a transaction
      Given A transaction
      When The customer asks for a refund
      Then The transaction should be refunded




