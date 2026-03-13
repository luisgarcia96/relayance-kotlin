Feature: Add customer
  In order to keep the CRM customer list up to date
  As a Relayance user
  I want to add a new customer from the application

  Scenario: Add a customer with valid information
    Given the customer list is displayed
    And I am on the add customer screen
    When I enter "John Doe" as the customer name
    And I enter "john.doe@example.com" as the customer email
    And I save the customer
    Then I return to the customer list
    And I see a message confirming that the customer was saved
    And I see "John Doe" in the customer list
    And I see "john.doe@example.com" for this customer

  Scenario: Refuse a customer when the email format is invalid
    Given the customer list is displayed
    And I am on the add customer screen
    When I enter "John Doe" as the customer name
    And I enter "invalid-email" as the customer email
    And I save the customer
    Then I remain on the add customer screen
    And I see a message indicating that the customer cannot be saved
    And I do not see "John Doe" in the customer list
