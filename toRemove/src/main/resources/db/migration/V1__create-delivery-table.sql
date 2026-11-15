CREATE TABLE delivery(
    id UUID PRIMARY KEY,
    item_name VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    weight NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    users_id UUID NOT NULL,
    users_name VARCHAR(50) NOT NULL,
    users_email VARCHAR(50) NOT NULL,
    address_id UUID NOT NULL,
    address_city VARCHAR(50) NOT NULL,
    address_cep VARCHAR(20) NOT NULL,
    address_number VARCHAR(20) NOT NULL
);