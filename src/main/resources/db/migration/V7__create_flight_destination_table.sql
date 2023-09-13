CREATE TABLE flight_reservations (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    flight_date TIMESTAMP NOT NULL,
    number_of_seats INT NOT NULL,
    total_price NUMERIC(10, 2),
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);
