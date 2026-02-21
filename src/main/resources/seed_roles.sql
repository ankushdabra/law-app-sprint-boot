-- Seed default roles.
-- Safe to run multiple times.
-- Postgres: uses gen_random_uuid() from pgcrypto.

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO roles (id, name)
SELECT gen_random_uuid(), 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_USER'
);

INSERT INTO roles (id, name)
SELECT gen_random_uuid(), 'ROLE_LEGAL'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_LEGAL'
);
