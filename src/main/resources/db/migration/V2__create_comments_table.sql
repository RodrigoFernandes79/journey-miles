 create table comments (
        id BIGSERIAL PRIMARY KEY,
        client_id BIGINT,
        comment varchar(255) NOT NULL,
        photo varchar(255),
        username varchar(255) NOT NULL,
        FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
    );