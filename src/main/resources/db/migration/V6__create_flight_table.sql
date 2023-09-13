CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    total_seats INT NOT NULL,
    airline VARCHAR(255) NOT NULL,
    aircraft_model VARCHAR(255) NOT NULL,
    sold_out BOOLEAN DEFAULT TRUE,
    destination_id BIGINT NOT NULL,
    FOREIGN KEY (destination_id) REFERENCES destinations(id)
);

