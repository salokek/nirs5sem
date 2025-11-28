CREATE TABLE IF NOT EXISTS autosalon.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- MANAGER (владелец, администратор)
INSERT INTO autosalon.users (username, password, role)
VALUES ('manager', '$2a$10$eBHF.8Fvt/QT4FXq7mPxOuuQHYy0S1tQ9Mjwml6e8Nf82OOP5pxJ2', 'MANAGER');

-- USER (обычный сотрудник)
INSERT INTO autosalon.users (username, password, role)
VALUES ('user', '$2a$10$Y9Q.gKvrDcqNtHhXgND0T.9EDV7p.RVY6U.Ux/N4Ft86mY2pK0PQi', 'USER');
