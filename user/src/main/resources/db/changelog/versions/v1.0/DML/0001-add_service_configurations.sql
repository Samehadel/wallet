-- liquibase formatted sql
-- changeset Sameh.Adel:0001-add_service_configurations

INSERT INTO SERVICE_CONFIGURATIONS (CONFIG_KEY, CONFIG_VALUE) VALUES ('MAX_LOGIN_TRIALS', '5');