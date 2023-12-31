package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.client.*;
import com.br.rodrigo.jornadamilhas.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Transactional
    public ResponseEntity<ClientDataOutput> createClient(
            @Valid @RequestBody ClientDataInput clientDataInput,
            UriComponentsBuilder uriComponentsBuilder) {

        var client = clientService.createClient(clientDataInput);
        var uri = uriComponentsBuilder.path("/clients/{id}")
                .buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientDataOutput(client));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<ListClientDataOutput>> listAllClients() {
        List<ListClientDataOutput> listClientDataOutputs = clientService.listAllClients();

        return ResponseEntity.ok().body(listClientDataOutputs);
    }

    @Secured("ROLE_USER")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<UpdateClientDataOutput> updateClient(@PathVariable Long id,
                                                               @Valid @RequestBody ClientDataInputUpdate clientDataInputUpdate) {
        var client = clientService.updateClient(id, clientDataInputUpdate);
        return ResponseEntity.ok().body(new UpdateClientDataOutput(client));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<UpdateClientDataOutput> findClientById(
            @PathVariable Long id) {
        var client = clientService.findClientById(id);
        return ResponseEntity.ok().body(new UpdateClientDataOutput(client));
    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteClientById(@PathVariable Long id) {
        clientService.deleteClientById(id);
        Map<String, String> message = new HashMap<>();
        message.put("Message", "Client deleted successfully");
        return ResponseEntity.ok().body(message);
    }
}
