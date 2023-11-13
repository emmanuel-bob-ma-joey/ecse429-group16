Feature: Create a Project

    As a user,
    I want to create a project
    so that I can keep track of my responsabilities.

    Background: The project application is running
        Given the application is running (US1)

    # Normal Flow
    Scenario Outline: Create a project
        Given the project with title "<title>" and the description "<description>" does not exist in the system (US1)
        When I create a project with the title "<title>" and description "<description>" (US1)
        Then the project with title "<title>" and the description "<description>" shall exist in the system (US1)
        Examples:
            | title             | description               |
            | My first project  | This is my first project  |
            | My second project | This is my second project |

    # Alternative Flow
    Scenario Outline: Create a project that has already been completed
        Given the project with title "<title>" does not exist in the system (US1)
        When I create a project with the title "<title>" and completed "<completed>" (US1)
        Then the project with title "<title>" with a completed "<completed>" shall exist in the system (US1)
        Examples:
            | title                  | completed |
            | A project with completed | true       |
            | Second Project             | true       |


    # Error Flow
    Scenario Outline: Create a project with an invalid id
        When I create a project with an invalid id (US1)
        Then the status code shall be <statusCode> with an error message "<errorMessage>" (US1)
        Examples:
            | id | statusCode | errorMessage                                |
            | asdf | 404        | No such project entity instance with GUID or ID asdf found |