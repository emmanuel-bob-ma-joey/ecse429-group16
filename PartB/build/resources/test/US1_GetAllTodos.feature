Feature: Get All Todos
    As a user, I want to see all todos so that I can see what I have to do.

    Background:
        Given the Todo application is running
    Scenario:
        Given the application is running
        Given the following Categories exist:
            | Name |
            | Work |
            | Home |

        Given the following Projects exist in the application:
            | Name      | Category |
            | Project 1 | Work     |
            | Project 2 | Home     |

        Given the following Todos exist in the application:
            | Name   | Project   | Category |
            | Todo 1 | Project 1 | Work     |
            | Todo 2 | Project 1 | Work     |
            | Todo 3 | Project 2 | Home     |
            | Todo 4 | Project 2 | Home     |

        #Success Case
        When I get all todos
        Then the response status code should be <statusCode>
            | statusCode |
            | 200        |
        Then the following information should be displayed:
            | Name   | Project   | Category |
            | Todo 1 | Project 1 | Work     |
            | Todo 2 | Project 1 | Work     |
            | Todo 3 | Project 2 | Home     |
            | Todo 4 | Project 2 | Home     |

        #Alternate case
        When I get