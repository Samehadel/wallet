-- liquibase formatted sql
-- changeset Sameh.Adel:0002-add_public_urls

INSERT INTO ACCESS_URL(URL, METHOD, PRIVATE)
VALUES ('/user/public/register', 'POST', 0);

INSERT INTO ACCESS_URL(URL, METHOD, PRIVATE)
VALUES ('/security/public/authenticate', 'POST', 0);