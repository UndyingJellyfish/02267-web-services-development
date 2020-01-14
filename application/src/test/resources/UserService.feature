Feature: Test for user service

  Scenario: A customer should be able to sign up
    Given The name of a customer
    When The customer signs up
    Then The customer should be signed up

  Scenario: A merchant should be able to sign up
    Given The name of a merchant
    When The merchant signs up
    Then The merchant should be signed up



