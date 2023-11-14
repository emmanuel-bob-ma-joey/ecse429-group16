Feature: Get a specific category

    As a user,
    I want to get a specific category,
    so that I can see the details of that category.

    Background: The todo application is running
        Given the todo application is running (US5)
        And the following categories exist in the system: (US5)
            | title                    | description  |
            | category1                | description1 |
            | category2                | description2 |
            | category3                | description3 |
            | emptyDescriptionCategory |              |

    # Normal Flow
    Scenario Outline: Get a specific category that
        Given the category with id <id> exists in the system (US5)
        When I get the category with id <id> (US5)
        Then I should get a category with id <id>, title "<title>", description "<description>"  (US5)

        Examples:
            | id | title     | description  |
            | 3  | category1 | description1 |
            | 4  | category2 | description2 |
            | 5  | category3 | description3 |

    # Alternate flow
    Scenario Outline: Get a category that has an empty description
        Given the category with id <id> and no description exists in the system (US5)
        When I get the category with id <id> that has no description (US5)
        Then I should get a category that has an empty description with id <id>, title "<title>" and description "<description>"  (US5)

        Examples:
            | id | title                    | description |
            | 6  | emptyDescriptionCategory |             |



    # Error Flow
    Scenario Outline: Get a category that does not exist
        Given the category with id <id> does not exist in the system (US5)
        When I get the category with invalid id <id> (US5)
        Then I should receive a <statusCode> status code and an error message "<errorMessage>" (US5)

        Examples:
            | id  | statusCode | errorMessage                                   |
            | 100 | 404        | Could not find an instance with categories/100 |