package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInput;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInputUpdate;
import com.br.rodrigo.jornadamilhas.domains.client.ListClientDataOutput;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.exceptions.PasswordNotEqualsException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client createClient(ClientDataInput clientDataInput) throws PasswordNotEqualsException {
        var verifyClient = clientRepository.findByEmail(clientDataInput.email());
        var verifyCpf = clientRepository.findByCpf(clientDataInput.cpf());
        if (verifyClient.isPresent() || verifyCpf.isPresent()) {
            throw new ExistingDataException("E-mail or CPF already registered");
        }
        if (!clientDataInput.password().equals(clientDataInput.repeatPassword())) {
            throw new PasswordNotEqualsException("Passwords must be equals!");
        }
        var client = clientRepository.save(new Client(clientDataInput));
        return client;
    }

    public List<ListClientDataOutput> listAllClients() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            new DataNotFoundException("No client found");
        }
        List<ListClientDataOutput> clientsOutput = clients
                .stream().map(ListClientDataOutput::new).toList();
        return clientsOutput;
    }

    public Client updateClient(Long id, ClientDataInputUpdate clientDataInputUpdate) {
        var verifyClient = clientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Client " + id + " not found"));

        verifyClient.dataUpdateClient(clientDataInputUpdate);
        return verifyClient;
    }

    public Client findClientById(Long id) {
        var verifyClient = clientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Client " + id + " Doesn't exists"));
        return verifyClient;
    }

    public void deleteClientById(Long id) {
        var verifyClient = clientRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Client doesnt exists"));
        clientRepository.delete(verifyClient);
    }


}
