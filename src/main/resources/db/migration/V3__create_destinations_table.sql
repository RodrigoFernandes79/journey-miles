 create table destinations (
        id BIGSERIAL PRIMARY KEY,
        name varchar(255) NOT NULL,
        photo varchar(255),
        price DECIMAL(10, 2) NOT NULL
    );