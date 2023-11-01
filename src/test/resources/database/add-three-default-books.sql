-- Insert categories
INSERT INTO categories (id, name, description, deleted) VALUES (1, 'Science Fiction', 'Fiction in which advanced technology and science are key elements of the plot.', false);
INSERT INTO categories (id, name, description, deleted) VALUES (2, 'Mystery', 'Fiction involving mysterious events and crime-solving detectives.', false);
INSERT INTO categories (id, name, description, deleted) VALUES (3, 'Fantasy', 'Fiction containing magical or supernatural elements.', false);

-- Insert books
INSERT INTO books (id, title, author, isbn, price, description, cover_image, deleted) VALUES (1, 'Dune', 'Frank Herbert', '9780441013593', 12.99, 'A science fiction novel set in a distant future where noble houses control planetary fiefs.', 'dune-cover.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, deleted) VALUES (2, 'The Hound of the Baskervilles', 'Arthur Conan Doyle', '9780486282145', 10.99, 'A Sherlock Holmes novel involving the mysterious death of a wealthy landowner.', 'hound-cover.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, deleted) VALUES (3, 'The Hobbit', 'J.R.R. Tolkien', '9780547928227', 15.99, 'A fantasy novel featuring the adventures of a hobbit named Bilbo Baggins.', 'hobbit-cover.jpg', false);

-- Insert book_category relations
INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
INSERT INTO book_category (book_id, category_id) VALUES (2, 2);
INSERT INTO book_category (book_id, category_id) VALUES (3, 3);
