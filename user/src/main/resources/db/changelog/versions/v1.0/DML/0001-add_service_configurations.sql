-- liquibase formatted sql
-- changeset Sameh.Adel:0001-create_initial_schema

INSERT INTO SERVICE_CONFIGURATIONS ([KEY], [VALUE]) VALUES ('MAX_LOGIN_TRIALS', '5');