CREATE TABLE collection(
    id UUID PRIMARY KEY,
    item_name VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    weight NUMERIC(10, 2) NOT NULL,
    users_id UUID,
    address_id UUID,

    FOREIGN KEY(users_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    FOREIGN KEY(address_id)
        REFERENCES address(id)
        ON DELETE CASCADE
);