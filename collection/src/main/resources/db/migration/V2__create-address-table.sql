CREATE TABLE address(
    id UUID PRIMARY KEY,
    users_id UUID,
    city VARCHAR(50) NOT NULL,
    cep VARCHAR(20) NOT NULL,
    number VARCHAR(10) NOT NULL,

    FOREIGN KEY(users_id)
        REFERENCES users(id)
        ON DELETE CASCADE

);