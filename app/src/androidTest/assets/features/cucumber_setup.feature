Feature: Cucumber Android setup

  Scenario: The Cucumber instrumentation environment starts correctly
    Given the Relayance test instrumentation is running
    Then the target application package is "com.kirabium.relayance"
