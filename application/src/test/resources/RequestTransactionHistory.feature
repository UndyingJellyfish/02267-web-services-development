Feature: Request Transaction History

  Background: Payment Service test cases

  # @author s164395
  Scenario: Customer Requests History
    Given A Customer with a transaction history
    When The Customer requests the transaction history
    Then The Customer receives the transaction history

  # @author s164410
  Scenario: Merchant Requests History
    Given A Merchant with a transaction history
    When The Merchant requests the transaction history
    Then The Merchant receives the transaction history without customer names