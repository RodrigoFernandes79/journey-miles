create table users (
        id BIGSERIAL PRIMARY KEY,
        name varchar(255) NOT NULL,
        email varchar(255) NOT NULL,
        password varchar(255) NOT NULL,
        role varchar(30) NOT NULL
          );