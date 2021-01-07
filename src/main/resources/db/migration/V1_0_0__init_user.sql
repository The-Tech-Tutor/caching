CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    create_date timestamp
);