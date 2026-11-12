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
- Update book details
- Get all books
- Get book by ID
- Search and filter books with pagination and sorting
- Track total and available physical copies
- Prevent issuing an unavailable book

### Member Management

- Add a member
- Get all members
- Get member by ID
- Update member contact details
- Activate or deactivate a member account
- Prevent duplicate email addresses
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
| POST | `/books` | Add a title and physical copies |
| GET | `/books` | Search books; supports `title`, `isbn`, `authorId`, `categoryId`, `page`, `size`, and `sort` |
| GET | `/books/{id}` | Get book by ID |
| GET | `/books/{id}/copies` | List physical copies and statuses |
| PUT | `/books/{id}` | Update title, ISBN, author, or category |
| DELETE | `/books/{id}` | Delete a book with no loan history |

### Authors and categories

| Method | Endpoint | Description |
|---|---|---|
| POST | `/authors` | Create an author |
| GET | `/authors` | List authors |
| GET | `/authors/{id}` | Get an author |
| PUT | `/authors/{id}` | Update an author |
| DELETE | `/authors/{id}` | Delete an author not linked to books |
| POST | `/categories` | Create a category |
| GET | `/categories` | List categories |
| GET | `/categories/{id}` | Get a category |
| PUT | `/categories/{id}` | Update a category |
| DELETE | `/categories/{id}` | Delete a category not linked to books |

### Members

| Method | Endpoint | Description |
|---|---|---|
| POST | `/members` | Register a member (starts as `ACTIVE`) |
| GET | `/members` | Get all members |
| GET | `/members/{id}` | Get member by ID |
| PUT | `/members/{id}` | Update name, email, and phone |
| PATCH | `/members/{id}/status` | Change status to `ACTIVE` or `INACTIVE` |
| DELETE | `/members/{id}` | Delete a member without loan history |

Inactive members cannot borrow books. Email addresses are trimmed, stored in lowercase, and must be unique.

### Issues

| Method | Endpoint | Description |
|---|---|---|
| POST | `/issues` | Issue a book |
| GET | `/issues` | Get all issue records |
| GET | `/issues/overdue` | Get active loans past their due date |
| GET | `/issues/{id}` | Get issue by ID |
| POST | `/issues/{id}/return` | Return a book |

The issue request body contains a `bookCopyId` and `memberId`. New loans get an issue date and a due date 14 days later by default. Configure `library.loan.period-days` to change this period. Responses include `dueDate` and a computed `overdue` flag; returned loans are never overdue. Loan records are history and do not have a delete endpoint.

## Business Logic

Each book title can have multiple physical copies. A copy can be issued only when its status is `AVAILABLE`; issue and return operations update that copy's status. A book with borrowing history cannot be deleted.

### Book API examples

Create a book and two physical copies with `POST /books`:

```json
{
  "title": "Pride and Prejudice",
  "isbn": "9780141439518",
  "authorId": 1,
  "categoryId": 1,
  "copies": 2
}
```

Update book metadata using `PUT /books/{id}`. Existing copies and their statuses are preserved:

```json
{
  "title": "Pride and Prejudice",
  "isbn": "9780141439518",
  "authorId": 1,
  "categoryId": 1
}
```

Search and page through books:

```text
GET /books?title=pride&categoryId=1&page=0&size=20&sort=title,asc
```

The `GET /books` response is a Spring `Page` JSON object with `content`, `totalElements`, `totalPages`, `size`, and `number`. Title and ISBN searches are case-insensitive partial matches; author and category filters match IDs exactly.

## Database

The application uses MySQL as the database and Hibernate/JPA for database interaction.

`Book` stores title and ISBN metadata, while each `BookCopy` tracks one physical item. Every book has one author and one category; each loan refers to one copy and one member.

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
