package com.br.rodrigo.jornadamilhas.domains.client;

import com.br.rodrigo.jornadamilhas.domains.address.Address;

public record UpdateClientDataOutput(String username, String photo,
                                     String phoneNumber, Address address) {
    public UpdateClientDataOutput(Client client) {
        this(client.getUsername(), client.getPhoto(), client.getPhoneNumber(), client.getAddress());
    }
}
