Feature: Payment Service Tests

  Background: Payment Service test cases


    Scenario: Creating a transaction
      Given A merchant, a valid token and a positive amount
      When The merchant initiates the transaction
      Then The transaction should go through

    Scenario: Creating an invalid transaction
      Given A merchant, a token which does not exist and a positive amount
      When The merchant initiates the invalid transaction
      Then The transaction should fail





