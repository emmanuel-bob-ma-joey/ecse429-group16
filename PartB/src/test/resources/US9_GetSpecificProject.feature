Feature: Get a specific project

    As a user,
    I want to get a specific project,
    so that I can see its details.

    Background: The project application is running
        Given the project application is running (US9)
        And the following projects exist in the system: (US9)
            | title       | description    | completed | active |
            | first proj  | fun proj       | false     | true   |
            | second proj | boring proj    | true      | false  |
            | third proj  | super fun proj | false     | false  |
            | fourth proj |                | true      | true   |

    # Normal Flow
    Scenario Outline: Get a specific project
        Given the project with id <id> exists in the system (US9)
        When I get the project with id <id> (US9)
        Then I should get a project with id <id>, title "<title>", description "<description>", completed "<completed>" and active "<active>" (US9)

        Examples:
            | id | title       | description    | completed | active |
            | 2  | first proj  | fun proj       | false     | true   |
            | 3  | second proj | boring proj    | true      | false  |
            | 4  | third proj  | super fun proj | false     | false  |

    # Alternate flow
    Scenario Outline: Get a project that has an empty description
        Given the project with id <id> and no description exists in the system (US9)
        When I get the project with id <id> that has no description (US9)
        Then I should get a project that has an empty description with id <id>, title "<title>", description "<description>", completed "<completed>" and active "<active>" (US9)

        Examples:
            | id | title       | description | completed | active |
            | 5  | fourth proj |             | true      | true   |



    # Error Flow
    Scenario Outline: Get a project that does not exist
        Given the project with id <id> does not exist in the system (US9)
        When I get the project with invalid id <id> (US9)
        Then I should receive a <statusCode> status code and an error message "<errorMessage>" (US9)

        Examples:
            | id  | statusCode | errorMessage                                 |
            | 100 | 404        | Could not find an instance with projects/100 |