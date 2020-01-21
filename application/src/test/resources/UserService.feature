Feature: Test for user service

  # @author s164410
  Scenario: A customer should be able to sign up
    Given The name of a customer
    When The customer signs up
    Then The customer should be signed up

  # @author s164410
  Scenario: A merchant should be able to sign up
    Given The name of a merchant
    When The merchant signs up
    Then The merchant should be signed up

  # @author s164410
  Scenario: A customer should be able to sign up
    Given The name of a customer
    And A bank account number
    When The customer signs up
    Then The customer should be signed up

  # @author s164410
  Scenario: A customer should be able to sign up
    Given The name of a merchant
    And A bank account number
    When The merchant signs up
    Then The merchant should be signed up

  # @author s164434
  Scenario: An account should be able to change their name
    Given An account
    When The user requests a name change
    Then The name should be changed

  # @author s164434
  Scenario: An account should be able to be deleted
    Given An account
    When The user requests to be deleted
    Then The user should be deleted, and unused tokens should be removed