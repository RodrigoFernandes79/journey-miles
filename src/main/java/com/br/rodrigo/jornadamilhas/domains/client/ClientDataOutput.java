package com.br.rodrigo.jornadamilhas.domains.client;

public record ClientDataOutput(String username, String photo) {
    public ClientDataOutput(Client client) {
        this(client.getUsername(), client.getPhoto());
    }
}
