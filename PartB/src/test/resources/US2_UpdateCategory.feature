Feature: Update category

    As a user,
    I want to be able to update a category
    so that I can modify its content whenever needed.

    Background: The todo application is running
        Given the todo application is running (US2)
        And the following categories exist in the system (US2)
            | title | description | 
            | category1 | description 1    | 
            | category2 | description 2    | 

    # Normal Flow
    Scenario Outline: Update a categories title and description
        Given a category with <id> exists in the system (US2)
        When I update the category with <id> with a new description "<description>" (US2)
        Then the todo with <id> should have title "<title>", description "<description>" (US2)
        Examples:
            | id | title | description |  
            | 3  | category1 | category 1  | 
            | 4  | category2 | category 2 | 

    # Alternate Flow
    Scenario Outline: update a category with only the description
        Given a incomplete category with id <id> exists in the system (US2)
        When I update the category with id <id> with "<description>" (US2)
        Then the category with <id> should have an updated description "<description>" (US2)
        Examples:
            | id | description |
            | 3  | category 1       |
            | 4  | category 2       |


    # Error Flow
    Scenario Outline: Update a category with empty title
        Given a category with <id> that exists in the system (US2)
        When I update the category with id <id> with empty title "<title>" (US2)
        Then I should receive a <status> response (US2)
        Examples:
            | status |                                 |
            | 400    | 

