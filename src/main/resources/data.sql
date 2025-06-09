INSERT INTO items (_id, amount, description) VALUES (1, 1000, 'Item 1');
INSERT INTO items (_id, amount, description) VALUES (2, 100, 'Item 2');
INSERT INTO items (_id, amount, description) VALUES (3, 10, 'Item 3');
INSERT INTO items (_id, amount, description) VALUES (4, 1, 'Item 4');
INSERT INTO items (_id, amount, description) VALUES (5, 123, 'Item 5');
ALTER TABLE items ALTER COLUMN _id RESTART WITH 6;