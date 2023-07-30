package com.br.rodrigo.jornadamilhas.domains.client;

import com.br.rodrigo.jornadamilhas.domains.address.Address;

public record ListClientDataOutput(Long id, String username, String photo,
                                   String phoneNumber, String email, String cpf,
                                   Address address) {

    public ListClientDataOutput(Client client) {
        this(client.getId(), client.getUsername(), client.getPhoto(), client.getPhoneNumber(),
                client.getEmail(), client.getCpf(), client.getAddress());
    }
}
