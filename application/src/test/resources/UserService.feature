Feature: Test for user service

  Scenario: A customer should be able to sign up
    Given The name of a customer
    When The customer signs up
    Then The customer should be signed up

  Scenario: A merchant should be able to sign up
    Given The name of a merchant
    When The merchant signs up
    Then The merchant should be signed up


  Scenario: An account should be able to change their name
    Given An account
    When The user requests a name change
    Then The name should be changed


  Scenario: An account should be able to be deleted
    Given An account
    When The user requests to be deleted
    Then The user should be deleted, and unused tokens should be removed