
CREATE TABLE todos(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    client VARCHAR(255),
    title TEXT,
    description TEXT,
    link VARCHAR(255),
    creation DATE,
    due DATE,
    status VARCHAR(255),
    priority VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);



