package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.destination.*;
import com.br.rodrigo.jornadamilhas.services.DestinationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/destinations")
public class DestinationController {
    @Autowired
    private DestinationService destinationService;

    @PostMapping
    @Transactional
    public ResponseEntity<DestinationDataOutput> createDestination(
            @Valid @RequestBody DestinationDataInput destinationDataInput,
            UriComponentsBuilder uriComponentsBuilder) {
        var destination = destinationService.createDestination(destinationDataInput);
        var uri = uriComponentsBuilder.path("/destination/{id}")
                .buildAndExpand(destination.getId()).toUri();

        return ResponseEntity.created(uri).body(new DestinationDataOutput(destination));
    }

    @GetMapping
    public ResponseEntity<Page<ListDestinationDataOutput>> listAllDestination(
            @PageableDefault(size = 6, sort = {"name"}) Pageable pageable) {
        Page<ListDestinationDataOutput> destination = destinationService
                .listAllDestination(pageable);

        return ResponseEntity.ok().body(destination);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDataOutput> findDestinationById(@PathVariable Long id) {
        DestinationDataOutput destination = destinationService.findDestinationById(id);

        return ResponseEntity.ok().body(destination);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ListDestinationDataOutput>> findDestinationByName(@RequestParam(name = "name", required = true) String name,
                                                                                 @PageableDefault(size = 6, sort = "price") Pageable pageable) {
        Page<ListDestinationDataOutput> listDestinations = destinationService
                .findDestinationByName(name, pageable);

        return ResponseEntity.ok().body(listDestinations);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<DestinationDataOutputUpdate> updateDestination(
            @PathVariable Long id, @RequestBody DestinationDataInputUpdate destinationDataInput) {
        DestinationDataOutputUpdate destination = destinationService.updateDestination(
                id, destinationDataInput);

        return ResponseEntity.ok().body(destination);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        Map<String, String> showMessage = new HashMap<>();
        showMessage.put("Message:", "Destination " + id + " deleted successfully!");

        return ResponseEntity.ok().body(showMessage);
    }
}

