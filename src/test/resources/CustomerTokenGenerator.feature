Feature: Customer Token Generator Feature

  Background: Token Generator Use Cases for Customers

  Scenario Outline: Request 1-5 Tokens
    Given A registered user
    And The user has spent all tokens
    When The user requests a <number> of tokens
    Then The user receives <number> tokens
  Examples:
    | number |
    |   1    |
    |   2    |
    |   3    |
    |   4    |
    |   5    |

  Scenario: Request 2 Tokens
    Given A registered user
    And The user has 1 unused token
    When The user requests 2 tokens
    Then The user has 3 unused tokens