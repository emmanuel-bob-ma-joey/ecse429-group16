Feature: Delete a specific todo

    As a user,
    I want to delete a specific todo,
    so that I can remove it from my list.

    Background: The todo application is running
        Given the todo application is running (US4)

    # Normal Flow
    Scenario Outline: Delete a completed todo
        Given the completed todo with id <id> exists in the system (US4)
        And the following completed todos exist in the system (US4)
            | title | description  | doneStatus |
            | todo1 | description1 | true       |
            | todo2 | description2 | true       |
        When I delete the completed todo with id <id> (US4)
        Then the completed todo with id <id> should not exist in the system (US4)
        Then there should be 1 todo in the system (US4)
        Examples:
            | id |
            | 3  |

    # Alternate flow
    Scenario Outline: Delete an incompleted todo
        Given the incompleted todo with id <id> exists in the system (US4)
        And the following incompleted todos exist in the system (US4)
            | title | description  | doneStatus |
            | todo1 | description1 | false      |
            | todo2 | description2 | false      |
        When I delete the incompleted todo with id <id> (US4)
        Then the incompleted todo with id <id> should not exist in the system (US4)
        Then there should be 1 todo in the current system (US4)
        Examples:
            | id |
            | 3  |


    # Error Flow
    Scenario Outline: Delete a todo that does not exist
        Given the todo with id <id> does not exist in the system (US4)
        When I delete the todo with inexistant id <id> (US4)
        Then I should receive a status code <statusCode> and see an error message "<errorMessage>" (US4)
        Examples:
            | id | statusCode | errorMessage                               |
            | 10 | 404        | Could not find any instances with todos/10 |
