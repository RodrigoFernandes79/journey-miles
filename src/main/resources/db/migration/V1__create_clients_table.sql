create table clients (
        id BIGSERIAL PRIMARY KEY,
         username varchar(255) NOT NULL,
         email varchar(255) NOT NULL,
         phone_number varchar(15),
         cpf varchar(11) NOT NULL,
         photo varchar(255),
        password varchar(255) NOT NULL,
        street_or_avenue varchar(200),
        district varchar(50),
        zip_code varchar(20),
        number varchar(10),
        apartment varchar(10),
        city varchar(30),
        state varchar(30)
    );