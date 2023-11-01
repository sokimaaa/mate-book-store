-- Delete book_category relations
DELETE FROM book_category WHERE book_id IN (4, 5);

-- Delete more books
DELETE FROM books WHERE id IN (4, 5);
