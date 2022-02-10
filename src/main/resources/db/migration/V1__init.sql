CREATE SEQUENCE orders_seq;
CREATE SEQUENCE orders_outbox_seq;

CREATE TABLE orders_inbox
(
    id VARCHAR(255) PRIMARY KEY NOT NULL UNIQUE
);

CREATE TABLE orders
(
    id     BIGSERIAL PRIMARY KEY,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE orders_outbox
(
    id             BIGSERIAL PRIMARY KEY,
    aggregate_id   VARCHAR(255) NOT NULL,
    aggregate_type VARCHAR(255) NOT NULL,
    type           VARCHAR(255) NOT NULL,
    payload        TEXT
)


