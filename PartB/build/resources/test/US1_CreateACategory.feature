# Feature: Create a Category

#     As a user,
#     I want to create a category
#     so that I can put different todos into different types.

#     Background: The todo application is running
#         Given the application is running (US1)

#     # Normal Flow
#     Scenario Outline: Create a category
#         Given the category with title "<title>" and the description "<description>" does not exist in the system (US1)
#         When I create a category with the title "<title>" and description "<description>" (US1)
#         Then the category with title "<title>" and the description "<description>" shall exist in the system (US1)
#         Examples:
#             | title              | description                |
#             | My first category  | This is my first category  |
#             | My second category | This is my second category |

#     # Alternative Flow
#     Scenario Outline: Create a category that has already been created
#         Given the category with title "<title>" does not exist in the system (US1)
#         When I create a duplicate category with the title "<title>" and description "<description>" (US1)
#         Then the category with title "<title>" with a description "<description>" shall exist in the system (US1)
#         Examples:
#             | title              | description                |
#             | My first category  | this is my first category  |
#             | My second category | this is my second category |


#     # Error Flow
#     Scenario Outline: Create a category with an empty title
#         When I create a category with empty title (US1)
#         Then the status code shall be <statusCode> with an error message "<errorMessage>" (US1)
#         Examples:
#             | statusCode | errorMessage                                |
#             | 400        | Failed Validation: title : can not be empty |