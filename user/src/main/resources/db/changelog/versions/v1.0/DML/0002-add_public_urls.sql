-- liquibase formatted sql
-- changeset Sameh.Adel:0002-add_public_urls

INSERT INTO ACCESS_URL(URL, METHOD, PRIVATE)
VALUES ('/api/v1/user/public/register', 'POST', false);