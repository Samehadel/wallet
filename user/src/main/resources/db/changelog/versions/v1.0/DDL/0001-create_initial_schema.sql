-- liquibase formatted sql
-- changeset Sameh.Adel:0001-create_initial_schema

CREATE TABLE USERS
(
    ID                 INT IDENTITY (1,1) PRIMARY KEY,
    USERNAME           VARCHAR(50)  NOT NULL UNIQUE,
    CIF                VARCHAR(20)  NOT NULL UNIQUE,
    MOBILE             VARCHAR(15)  NOT NULL UNIQUE,
    FIRST_NAME         VARCHAR(50)  NOT NULL,
    LAST_NAME          VARCHAR(50)  NOT NULL,
    PASSWORD           VARCHAR(255) NOT NULL,
    STATUS             VARCHAR(20)  NOT NULL,
    LAST_LOGIN_DATE    DATETIME      NULL,
    LOGIN_TRIALS       INT           NOT NULL DEFAULT 0,
    STATUS_UPDATED_AT  DATETIME      NULL,
    CREATED_AT         DATETIME      NOT NULL,
    LAST_MODIFIED_DATE DATETIME      NULL,
    ACTIVATED          BIT           NOT NULL DEFAULT 0,
    ACTIVATED_AT       DATETIME      NULL
);

CREATE TABLE ROLE
(
    ID         INT IDENTITY (1,1) PRIMARY KEY,
    ROLE_NAME  VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE ACCESS_URL
(
    ID         INT IDENTITY (1,1) PRIMARY KEY,
    URL        VARCHAR(255) NOT NULL,
    METHOD     VARCHAR(10)  NOT NULL,
    PRIVATE    BIT           NOT NULL DEFAULT 0
);

CREATE TABLE ROLE_ACCESS_URL
(
    ROLE_ID    INT      NOT NULL,
    URL_ID     INT      NOT NULL,
    PRIMARY KEY (ROLE_ID, URL_ID),
    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (URL_ID) REFERENCES ACCESS_URL (ID) ON DELETE CASCADE,
);

CREATE TABLE USER_ROLE
(
    USER_ID    INT      NOT NULL,
    ROLE_ID    INT      NOT NULL,
    PRIMARY KEY (USER_ID, ROLE_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID) ON DELETE CASCADE
);


CREATE TABLE SERVICE_CONFIGURATIONS
(
    ID    INT PRIMARY KEY IDENTITY (1,1),
    [KEY]  VARCHAR(50) UNIQUE NOT NULL,
    [VALUE] VARCHAR(255) NOT NULL
);