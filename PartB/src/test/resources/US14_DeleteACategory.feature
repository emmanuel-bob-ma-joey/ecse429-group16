Feature: Delete a specific category

    As a user,
    I want to delete a specific category,
    so that I can remove it from my list.

    Background: The todo application is running
        Given the todo application is running (US14)

    # Normal Flow
    Scenario Outline: Delete a category
       
        Given the following categories exist in the system (US14)
            | title     | description  |
            | category1 | description1 |
            | category2 | description2 |
        And the category with id <id> exists in the system (US14)
        When I delete the category with id <id> (US14)
        Then the category with id <id> should not exist in the system (US14)
        Then there should be 1 todo in the system (US14)
        Examples:
            | id |
            | 1  |

    # Alternate flow
    Scenario Outline: Delete a category that has an empty description
        #Given the category with id <id>  no description exists in the system (US4)
        Given the following incomplete categories exist in the system (US14)
            | title | description  |
            | todo1 | description1 |
            | todo2 |     ""         |

        When I delete the incompleted category with id <id> (US14)
        Then the incompleted category with id <id> should not exist in the system (US14)
        Then there should be 1 todo in the current system (US14)
        Examples:
            | id |
            | 3  |


    # Error Flow
    Scenario Outline: Delete a category that does not exist
        Given the category with id <id> does not exist in the system (US14)
        When I delete the category with inexistant id <id> (US14)
        Then I should receive a status code <statusCode> and see an error message "<errorMessage>" (US14)
        Examples:
            | id | statusCode | errorMessage                                    |
            | 100 | 404        | Could not find any instances with categories/100 |