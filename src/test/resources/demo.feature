Feature: Demo Feature
  Scenario Outline:
    Given Some "<string>"
    When Adding with StringCalc
    Then Give <answer>

  Examples:
    | string | answer |
    |        | 0      |
    | 1      | 1      |
    | 1,2    | 3      |
    | 2,3    | 5      |
    | 1\n2   | 3      |

