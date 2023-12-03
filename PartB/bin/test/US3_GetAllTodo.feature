Feature: Get All Todos

    As a user,
    I want to be able to get all todos
    so that I can see everything I have to do.

    Background: The todo application is running
        Given the todo application is running (US3)
        And the following todos exist in the system (US3)
            | title | description  | doneStatus |
            | todo1 | description1 | false      |
            | todo2 | description2 | true       |
            | todo3 | description3 | false      |

    # Normal flow
    Scenario Outline: Get all todos
        When I get all todos (US3)
        Then the todos with title "<title>", description "<description>" and doneStatus "<doneStatus>" (US3)
        Examples:
            | title | description  | doneStatus |
            | todo1 | description1 | false      |
            | todo2 | description2 | true       |
            | todo3 | description3 | false      |

    # Alternate flow
    Scenario Outline: Get all todos with a specific status
        When I get all todos with status doneStatus "<doneStatus>" (US3)
        Then I should see 2 todos (US3)
        Then the todos with title "<title>", description "<description>" and incomplete doneStatus "<doneStatus>" (US3)
        Examples:
            | title | description  | doneStatus |
            | todo1 | description1 | false      |
            | todo3 | description3 | false      |

    # Error flow
    Scenario Outline: Get all todos with the wrong endpoint
        When I get all todos with mistyped endpoint (US3)
        Then I should receive an error code <statusCode> (US3)
        Examples:
            | statusCode |
            | 404        |



