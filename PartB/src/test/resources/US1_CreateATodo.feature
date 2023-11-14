Feature: Create a Todo

    As a user,
    I want to create a todo
    so that I can keep track of my tasks.

    Background: The todo application is running
        Given the application is running (US1)

    # Normal Flow
    Scenario Outline: Create a todo
        Given the todo with title "<title>" and the description "<description>" does not exist in the system (US1)
        When I create a todo with the title "<title>" and description "<description>" (US1)
        Then the todo with title "<title>" and the description "<description>" shall exist in the system (US1)
        Examples:
            | title          | description            |
            | My first todo  | This is my first todo  |
            | My second todo | This is my second todo |

    # Alternative Flow
    Scenario Outline: Create a todo that has already been completed
        Given the todo with title "<title>" does not exist in the system (US1)
        When I create a todo with the title "<title>" and doneStatus "<doneStatus>" (US1)
        Then the todo with title "<title>" with a doneStatus "<doneStatus>" shall exist in the system (US1)
        Examples:
            | title                  | doneStatus |
            | A todo with DoneStatus | true       |
            | Finish US1             | true       |


    # Error Flow
    Scenario Outline: Create a todo with an empty title
        When I create a todo with empty title (US1)
        Then the status code shall be <statusCode> with an error message "<errorMessage>" (US1)
        Examples:
            | statusCode | errorMessage                                |
            | 400        | Failed Validation: title : can not be empty |