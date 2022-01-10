Feature: Escenario de ayuda para crear el token

  Scenario Outline: Login (creation de session)
    Given I set request with "<username>" and "<password>"
    And I use header auth
    When I call  post request CreateToken
    Then I get status code 200
    Examples:
      | username | password    |
      | admin    | password123 |