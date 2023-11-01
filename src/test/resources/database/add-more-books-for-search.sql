-- Insert more books
INSERT INTO books (id, title, author, isbn, price, description, cover_image, deleted) VALUES (4, 'The Godmakers', 'Frank Herbert', '9780441014811', 12.99, 'The sequel to Dune, continuing the story of Paul Atreides.', 'dune-messiah-cover.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, deleted) VALUES (5, 'Children of Dune', 'Frank Herbert', '9780441104024', 10.99, 'The third book in the Dune series.', 'children-of-dune-cover.jpg', false);

-- Insert book_category relations
INSERT INTO book_category (book_id, category_id) VALUES (4, 1);
INSERT INTO book_category (book_id, category_id) VALUES (5, 1);
