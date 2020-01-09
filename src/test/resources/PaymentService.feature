Feature: Payment Service Tests

  Background: Payment Service test cases


    Scenario: Creating a transaction
      Given A merchant, a valid token and a positive amount
      When The merchant initiates the transaction
      Then The transaction should go through


