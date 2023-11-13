Feature: Get All Projects

    As a user,
    I want to be able to get all projects
    so that I can see what I am working on.

    Background: The project application is running
        Given the project application is running (US3)
        And the following projects exist in the system (US3)
            | title     | description   | completed  | active |
            | project 1 | description 1 | false      | false  |
            | project 2 | description 2 | true       | true   |
            | project 3 | description 3 | false      |  true  |

    # Normal flow
    Scenario Outline: Get all projects
        When I get all projects (US3)
        Then the projects with title "<title>", description "<description>", completed "<completed>" and active "<active>" (US3)
        Examples:
            | title     | description   | completed  | active |
            | project 1 | description 1 | false      | false  |
            | project 2 | description 2 | true       | true   |
            | project 3 | description 3 | false      |  true  |

    # Alternate flow
    Scenario Outline: Get all projects with completed is true
        When I get all projects with completed is true (US3)
        Then I should see 1 project (US3)
        Then the projects with title "<title>", description "<description>", and active "<active>" (US3)
        Examples:
            | project 2 | description 2 | true       | true   |

    # Error flow
    Scenario Outline: Get all projects with the wrong endpoint
        When I get all projects with mistyped endpoint (US3)
        Then I should receive error code <statusCode> (US3)
        Examples:
            | statusCode |
            | 404        |