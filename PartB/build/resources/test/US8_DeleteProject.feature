Feature: Delete a specific project

    As a user,
    I want to delete a specific project,
    so that I can focus on other projects.

    Background: The project application is running
        Given the project application is running (US8)

    # Normal Flow
    Scenario Outline: Delete an active project
        Given the active project with id <id> exists in the system (US8)
        And the following active projects exist in the system (US8)
            | title          | description        | completed | active |
            | first project  | description proj 1 | true      | true   |
            | second project | description proj 2 | false     | true   |
        When I delete the active project with id <id> (US8)
        Then the active project with id <id> should not exist in the system (US8)
        Then there should be 1 active project in the system (US8)
        Examples:
            | id |
            | 3  |

    # Alternate flow
    Scenario Outline: Delete a completed project
        Given the completed project with id <id> exists in the system (US8)
        And the following completed projects exist in the system (US8)
            | title | description         | completed | active |
            | proj 1 | first description  | true      | false  |
            | proj 2 | second description | true      | true   |
        When I delete the completed project with id <id> (US8)
        Then the completed project with id <id> should not exist in the system (US8)
        Then there should be 1 project in the current system (US8)
        Examples:
            | id |
            | 3  |


    # Error Flow
    Scenario Outline: Delete a project that does not exist
        Given the project with id <id> does not exist in the system (US8)
        When I delete the project with inexistant id <id> (US8)
        Then I should receive a status code <statusCode> and see an error message "<errorMessage>" (US8)
        Examples:
            | id | statusCode | errorMessage                               |
            | 10 | 404        | Could not find any instances with projects/10 |