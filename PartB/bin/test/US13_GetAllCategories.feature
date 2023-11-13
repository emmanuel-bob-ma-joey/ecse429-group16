Feature: Get All Categories

    As a user,
    I want to be able to get all categories
    so that I can see everything I have to do.

    Background: The todo application is running
        Given the todo application is running (US3)
        And the following categories exist in the system (US3)
            | title     | description  |
            | category1 | description1 |
            | category2 | description2 |
            | category3 | description3 |

    # Normal flow
    Scenario Outline: Get all categories
        When I get all categories (US3)
        Then the categories with title "<title>", description "<description>" exist (US3)
        Examples:
            | title     | description  |
            | category1 | description1 |
            | category2 | description2 |
            | category3 | description3 |

    # Alternate flow
    Scenario Outline: Get all categories with a specific title
        When I get all categories with a specific title "<title>" (US3)
        Then I should see <num> categories with title "<title>" (US3)
        # Then the categories with title "<title>", and description "<description>" (US3)
        Examples:
            | title | num  | 
            | category1 | 1 | 

    # Error flow
    Scenario Outline: Get all categories with the wrong endpoint
        When I get all categories with mistyped endpoint (US3)
        Then I should receive an error code <statusCode> (US3)
        Examples:
            | statusCode |
            | 404        |


