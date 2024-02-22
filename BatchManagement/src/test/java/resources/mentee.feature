Feature: execute batch service

  Scenario: get batch by id
    Given batch 1
    When requested to find batch
    Then the status code should be 200
