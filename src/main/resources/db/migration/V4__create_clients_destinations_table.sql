CREATE TABLE clients_destinations (
    client_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    PRIMARY KEY (client_id, destination_id),
    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (destination_id) REFERENCES destinations (id) ON DELETE CASCADE
);



