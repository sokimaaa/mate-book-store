# Functional Requirements for the Online Bookstore

## Actors

- **User (Customer)**: The person who will be browsing books, adding them to the shopping cart, and placing orders.
- **Admin (Operator)**: The person who will be managing the book catalog, categories, and monitoring orders.

## Domain Models

- **User**: Contains information about the registered user including their authentication details and personal
  information.
- **Role**: Represents the role of a user in the system, for example, admin or user.
- **Book**: Represents a book available in the store.
- **Category**: Represents a category that a book can belong to.
- **ShoppingCart**: Represents a user's shopping cart.
- **CartItem**: Represents an item in a user's shopping cart.
- **Order**: Represents an order placed by a user.
- **OrderItem**: Represents an item in a user's order.

## Use Cases

1. **User Registration and Login**

- As a User, I want to register, so I can place orders. I will:
  1. Send a `POST` request to `/api/auth/registration` with my details.
  2. Receive a confirmation of successful registration.
- As a User, I want to log in, so I can browse the book catalog, manage my shopping cart, and place orders. I will:
  1. Send a `POST` request to `/api/auth/login` with my login credentials.
  2. Receive a token for authenticating my subsequent requests.

2. **Book Browsing and Searching**

- As a User, I want to browse the book catalog, so I can find books to purchase. I will:
  1. Send a `GET` request to `/api/books` to retrieve the book catalog.
- As a User, I want to view book details, so I can decide whether to purchase it. I will:
  1. Send a `GET` request to `/api/books/{id}` to retrieve the details of a specific book.
- [OPTIONAL]: As a User, I want to search books, so I can decide whether to purchase it. I will:
  1. Send a `GET` request to `/api/books/search` to retrieve the book catalog based on search params.

3. **Category Browsing**

- As a User, I want to browse categories, so I can find books by category. I will:
  1. Send a `GET` request to `/api/categories` to retrieve all categories.
  2. Send a `GET` request to `/api/categories/{id}/books` to retrieve books by a specific category.

4. **Shopping Cart Management**

- As a User, I want to add books to my shopping cart, so I can purchase them. I will:
  1. Send a `POST` request to `/api/cart` to add a book to the shopping cart.
- As a User, I want to view my shopping cart, so I can review its contents before placing an order. I will:
  1. Send a `GET` request to `/api/cart` to retrieve my shopping cart.
- As a User, I want to remove books from my shopping cart, so I can update my intended purchases. I will:
  1. Send a `DELETE` request to `/api/cart/books/{id}` to remove a book from the shopping cart.

5. **Order Processing**

- As a User, I want to place an order, so I can purchase the books in my shopping cart. I will:
  1. Send a `POST` request to `/api/orders` to place an order.
- As a User, I want to view my order history, so I can track my past purchases. I will:
  1. Send a `GET` request to `/api/orders` to retrieve my order history.

6. **Order Item Retrieval**

- As a User, I want to view the items in my order, so I can check the details of my purchase. I will:
  1. Send a `GET` request to `/api/orders/{orderId}/items` to retrieve all OrderItems for a specific order.
- As a User, I want to view a specific item in my order, so I can check its details. I will:
  1. Send a `GET` request to `/api/orders/{orderId}/items/{id}` to retrieve a specific OrderItem within an order.

## Admin Use Cases

1. **Book Management**

- As an Admin, I want to add a new book to the catalog so users can purchase it. I will:
  1. Send a `POST` request to `/api/books` with the details of the new book.
- As an Admin, I want to update the details of a book so the catalog is up-to-date. I will:
  1. Send a `PUT` request to `/api/books/{id}` with the updated details of the book.
- As an Admin, I want to remove a book from the catalog so users can no longer purchase it. I will:
  1. Send a `DELETE` request to `/api/books/{id}` to remove the book.

2. **Category Management**

- As an Admin, I want to create a new category so books can be categorized. I will:
  1. Send a `POST` request to `/api/categories` with the details of the new category.
- As an Admin, I want to update the details of a category so the categories are up-to-date. I will:
  1. Send a `PUT` request to `/api/categories/{id}` with the updated details of the category.
- As an Admin, I want to remove a category, so it is no longer available. I will:
  1. Send a `DELETE` request to `/api/categories/{id}` to remove the category.

3. **Order Management**

- As an Admin, I want to update order status, so I can manage the order processing workflow. I will:
  1. Send a `PATCH` request to `/api/orders/{id}` to update the status of an order.

# Non-Functional Requirements for the Online Bookstore

1. Security should be implemented (JWT)
2. Swagger documentation should be added for each controller
3. Pagination should be implemented for all endpoints where list is returned
4. Unit and Integration tests should be added. At least 80% lines of code should be covered with tests
5. Use TestContainers in your repository tests or integration tests
