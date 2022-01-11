Feature: Feature for endpoint to confirm whether the API is up and running

  Scenario:
    Given the API is up and running
    When I call get request HealthCheck
    Then I get message created