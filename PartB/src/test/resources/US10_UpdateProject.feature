Feature: Update project

    As a user,
    I want to be able to update a project
    so that I can modify its content.

    Background: The project application is running
        Given the project application is running (US10)
        And the following projects exist in the system (US10)
            | title       | description | completed | active |
            | first proj  | fun proj    | false     | true   |
            | second proj | boring proj | true      | false  |

    # Normal Flow
    Scenario Outline: Update a project's title and description
        Given a project with <id> exists in the system (US10)
        When I update the project with <id> with a new description "<description>" (US10)
        Then the project with <id> should have title "<title>", description "<description>", completed "<completed>" and active "<active>" (US10)
        Examples:
            | id | title       | description    | completed | active |
            | 2  | first proj  | fun project    | false     | true   |
            | 3  | second proj | boring project | true      | false  |

    # Alternate Flow
    Scenario Outline: Mark a project as completed
        Given a incomplete project with id <id> exists in the system (US10)
        When I update the project with id <id> with "<completed>" (US10)
        Then the project with <id> should have an updated completed "<completed>" (US10)
        Examples:
            | id | completed |
            | 2  | true      |


    # Error Flow
    Scenario Outline: Update completed to invalid value
        Given a project with <id> that exists in the system (US10)
        When I update the project with id <id> with invalid completed "<completed>" (US10)
        Then I should receive a <status> response with "<errorMessage>" (US10)
        Examples:
            | id | completed | status | errorMessage                                   |
            | 1  | asd       | 400    | Failed Validation: completed should be BOOLEAN |