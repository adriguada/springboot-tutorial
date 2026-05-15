-- Category
INSERT INTO category(name)
VALUES ('Eurogames');

INSERT INTO category(name)
VALUES ('Ameritrash');

INSERT INTO category(name)
VALUES ('Familiar');

-- Author
INSERT INTO author(name, nationality)
VALUES ('Alan R. Moon', 'US');

INSERT INTO author(name, nationality)
VALUES ('Vital Lacerda', 'PT');

INSERT INTO author(name, nationality)
VALUES ('Simone Luciani', 'IT');

INSERT INTO author(name, nationality)
VALUES ('Perepau Llistosella', 'ES');

INSERT INTO author(name, nationality)
VALUES ('Michael Kiesling', 'DE');

INSERT INTO author(name, nationality)
VALUES ('Phil Walker-Harding', 'US');

-- Game
INSERT INTO game(title, age, category_id, author_id)
VALUES ('On Mars', '14', 1, 2);

INSERT INTO game(title, age, category_id, author_id)
VALUES ('Aventureros al tren', '8', 3, 1);

INSERT INTO game(title, age, category_id, author_id)
VALUES ('1920: Wall Street', '12', 1, 4);

INSERT INTO game(title, age, category_id, author_id)
VALUES ('Barrage', '14', 1, 3);

INSERT INTO game(title, age, category_id, author_id)
VALUES ('Los viajes de Marco Polo', '12', 1, 3);

INSERT INTO game(title, age, category_id, author_id)
VALUES ('Azul', '8', 3, 5);

-- Customer
INSERT INTO customer(name)
VALUES ('David');

INSERT INTO customer(name)
VALUES ('Juan');

INSERT INTO customer(name)
VALUES ('José');

INSERT INTO customer(name)
VALUES ('Alberto');

INSERT INTO customer(name)
VALUES ('Javier');

INSERT INTO customer(name)
VALUES ('Francisco');

INSERT INTO customer(name)
VALUES ('Orlando');

INSERT INTO customer(name)
VALUES ('Miguel');

-- Lending
INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-05-01', '2026-05-05', 1, 1);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-05-01', '2026-05-15', 2, 2);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-05-05', '2026-05-10', 3, 1);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-04-01', '2026-05-03', 4, 2);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-04-01', '2026-05-20', 5, 4);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-04-06', '2026-05-25', 6, 3);

INSERT INTO lending(loan_date, return_date, game_id, customer_id)
VALUES ('2026-04-01', '2026-05-01', 1, 2);