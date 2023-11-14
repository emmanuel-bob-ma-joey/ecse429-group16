Feature: Update category

    As a user,
    I want to be able to update a category
    so that I can modify its content whenever needed.

    Background: The todo application is running
        Given the todo application is running (US12)
        And the following categories exist in the system (US12)
            | title     | description   |
            | category1 | description 1 |
            | category2 | description 2 |

    # Normal Flow
    Scenario Outline: Update a categories title and description
        Given a category with <id> exists in the system (US12)
        When I update the category with id <id> with a new title "<title>", description "<description>" (US12)
        Then the category with id <id> should have title "<title>", description "<description>" (US12)
        Examples:
            | id | title     | description |
            | 3  | category1 | category 1  |
            | 4  | category2 | category 2  |

    # Alternate Flow
    Scenario Outline: update a category with only the title
        Given a category with id <id> exists in the system (US12)
        When I update the category with id <id> with a title "<title>" and empty description (US12)
        Then the category with <id> should have an updated title "<title>" (US12)
        Examples:
            | id | title |
            | 3  | new category 1  |
            | 4  | new category 2  |


    # Error Flow
    Scenario Outline: Update a category with empty title
        Given a category with <id> that exists in the system (US12)
        When I update the category with id <id> with an empty title (US12)
        Then I should receive a <status> response (US12)
        Examples:
            |id | status | 
            |1  | 400    |       

