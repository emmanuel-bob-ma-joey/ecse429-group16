Feature: Get a specific todo

    As a user,
    I want to get a specific todo,
    so that I can see the details of that todo.

    Background: The todo application is running
        Given the todo application is running (US5)
        And the following todos exist in the system: (US5)
            | title                | description  | doneStatus |
            | todo1                | description1 | false      |
            | todo2                | description2 | true       |
            | todo3                | description3 | false      |
            | emptyDescriptionTodo |              | false      |

    # Normal Flow
    Scenario Outline: Get a specific todo that
        Given the todo with id <id> exists in the system (US5)
        When I get the todo with id <id> (US5)
        Then I should get a todo with id <id>, title "<title>", description "<description>" and doneStatus "<doneStatus>" (US5)

        Examples:
            | id | title | description  | doneStatus |
            | 3  | todo1 | description1 | false      |
            | 4  | todo2 | description2 | true       |
            | 5  | todo3 | description3 | false      |

    # Alternate flow
    Scenario Outline: Get a todo that has an empty description
        Given the todo with id <id> and no description exists in the system (US5)
        When I get the todo with id <id> that has no description (US5)
        Then I should get a todo that has an empty description with id <id>, title "<title>", description "<description>" and doneStatus "<doneStatus>" (US5)

        Examples:
            | id | title                | description | doneStatus |
            | 6  | emptyDescriptionTodo |             | false      |



    # Error Flow
    Scenario Outline: Get a todo that does not exist
        Given the todo with id <id> does not exist in the system (US5)
        When I get the todo with invalid id <id> (US5)
        Then I should receive a <statusCode> status code and an error message "<errorMessage>" (US5)

        Examples:
            | id  | statusCode | errorMessage                              |
            | 100 | 404        | Could not find an instance with todos/100 |
