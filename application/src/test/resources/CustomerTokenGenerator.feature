Feature: Customer Token Generator Feature

  Background: Token Generator Use Cases for Customers

  Scenario Outline: Request 1-5 Tokens
    Given A registered user
    When The user requests <number> of tokens
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
    When The user requests 2 of tokens
    Then The user has 3 unused tokens

  Scenario: Request tokens when having more than 1 tokens
    Given A registered user
    And The user already has 2 unused tokens
    When The user requests 1 tokens
    Then It should fail

  Scenario Outline: User requests illegal amount of tokens
    Given A registered user
    And the user has <number> unused tokens
    When The user requests <additional> tokens
    Then It should fail
    Examples:
      | number | additional |
      |   1    |   6        |
      |   2    |   1        |
      |   0    |   6        |
      |   0    |   0        |
