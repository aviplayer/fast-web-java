/* Event table */
CREATE TABLE IF NOT EXISTS "event"
(
    "id"             BIGSERIAL PRIMARY KEY,
    "name"           VARCHAR  NOT NULL,
    "email"          VARCHAR  NOT NULL,
    "age"            INT NOT NULL
);
