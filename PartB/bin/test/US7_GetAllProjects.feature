Feature: Get All Projects

    As a user,
    I want to be able to get all projects
    so that I can see what I am working on.

    Background: The project application is running
        Given the project application is running (US7)
        And the following projects exist in the system (US7)
            | title     | description   | completed  | active |
            | project 1 | description 1 | false      | false  |
            | project 2 | description 2 | true       | true   |
            | project 3 | description 3 | false      |  true  |

    # Normal flow
    Scenario Outline: Get all projects
        When I get all projects (US7)
        Then the projects with title "<title>", description "<description>", completed "<completed>" and active "<active>" (US7)
        Examples:
            | title     | description   | completed  | active |
            | project 1 | description 1 | false      | false  |
            | project 2 | description 2 | true       | true   |
            | project 3 | description 3 | false      |  true  |

    # Alternate flow
    Scenario Outline: Get all projects with completed is true
        When I get all projects with completed is true (US7)
        Then I should see 1 project (US7)
        Then the projects with title "<title>", description "<description>", and active "<active>" (US7)
        Examples:
            | title     | description   | active |
            | project 2 | description 2 | true   |

    # Error flow
    Scenario Outline: Get all projects with the wrong endpoint
        When I get all projects with mistyped endpoint (US7)
        Then I should receive error code <statusCode> (US7)
        Examples:
            | statusCode |
            | 404        |