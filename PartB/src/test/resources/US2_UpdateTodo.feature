Feature: Update todo

    As a user,
    I want to be able to update a todo
    so that I can modify its content whenever needed.

    Background: The todo application is running
        Given the todo application is running (US2)
        And the following todos exist in the system (US2)
            | title | description | doneStatus |
            | todo1 | not done    | false      |
            | todo2 | not done    | false      |

    # Normal Flow
    Scenario Outline: Update a todo
        Given a todo with <id> exists in the system (US2)
        When I update the todo with <id> with a new description "<description>" (US2)
        Then the todo with <id> should have title "<title>", description "<description>", status "<doneStatus>" (US2)
        Examples:
            | id | title | description | doneStatus |
            | 3  | todo1 | almost done | false      |
            | 4  | todo2 | almost done | false      |

    # Alternate Flow
    Scenario Outline: Mark a tasks as done
        Given a incomplete todo with id <id> exists in the system (US2)
        When I update the todo with id <id> with "<doneStatus>" (US2)
        Then the todo with <id> should have an updated status "<doneStatus>" (US2)
        Examples:
            | id | doneStatus |
            | 3  | true       |
            | 4  | true       |


    # Error Flow
    Scenario Outline: Update doneStatus to invalid value
        Given a todo with <id> that exists in the system (US2)
        When I update the todo with id <id> with invalid doneStatus "<doneStatus>" (US2)
        Then I should receive a <status> response with "<errorMessage>" (US2)
        Examples:
            | id | doneStatus | status | errorMessage                                    |
            | 1  | asd        | 400    | Failed Validation: doneStatus should be BOOLEAN |


