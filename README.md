# Library Management System

A RESTful Library Management System built using Java and Spring Boot.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- REST APIs
- Postman

## Architecture

The application follows a layered architecture:

Controller → Service → Repository → Database

### Controller Layer
Handles HTTP requests and responses.

### Service Layer
Contains the business logic of the application.

### Repository Layer
Communicates with the database using Spring Data JPA.

### Entity Layer
Represents database tables.

### DTO Layer
Controls the data returned by REST APIs.

### Exception Layer
Handles application errors and validation errors.

## Features

### Book Management

- Add a book
- Get all books
- Get book by ID
- Track book availability
- Prevent issuing an unavailable book

### Member Management

- Add a member
- Get all members
- Get member by ID
- Validate member information

### Issue Management

- Issue a book to a member
- Get all issue records
- Get issue by ID
- Return a book
- Automatically update book availability

### Validation

The application validates:

- Required book fields
- Required member fields
- Email format
- Phone number format
- Required book and member while issuing

### Exception Handling

The application provides meaningful errors for:

- Book not found
- Member not found
- Issue record not found
- Book already issued
- Invalid request data

## API Endpoints

### Books

| Method | Endpoint | Description |
|---|---|---|
| POST | `/books` | Add a book |
| GET | `/books` | Get all books |
| GET | `/books/{id}` | Get book by ID |

### Members

| Method | Endpoint | Description |
|---|---|---|
| POST | `/members` | Add a member |
| GET | `/members` | Get all members |
| GET | `/members/{id}` | Get member by ID |

### Issues

| Method | Endpoint | Description |
|---|---|---|
| POST | `/issues` | Issue a book |
| GET | `/issues` | Get all issue records |
| GET | `/issues/{id}` | Get issue by ID |
| POST | `/issues/{id}/return` | Return a book |

## Business Logic

When a book is issued:

Book availability changes:

`true → false`

When the book is returned:

`false → true`

A book that is already issued cannot be issued again.

## Database

The application uses MySQL as the database and Hibernate/JPA for database interaction.

## Testing

REST APIs were tested using Postman.

Tested scenarios include:

- Successful book creation
- Successful member creation
- Book issuing
- Book returning
- Book availability
- Invalid input validation
- Book not found
- Member not found
- Issue record not found
- Already-issued book

## How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure MySQL database details in `application.properties`.
4. Make sure MySQL is running.
5. Run the Spring Boot application.
6. Test the APIs using Postman.

## Project Architecture

```text
Client / Postman
       ↓
Controller
       ↓
Service
       ↓
Repository
       ↓
MySQL Database


AUTHOR:
MANOGNA K V
