package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.flight.FlightDataInput;
import com.br.rodrigo.jornadamilhas.domains.flight.FlightDataOutput;
import com.br.rodrigo.jornadamilhas.domains.flight.FlightDataUpdateDateTime;
import com.br.rodrigo.jornadamilhas.domains.flight.FlightSetOutput;
import com.br.rodrigo.jornadamilhas.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Secured("ROLE_ADMIN")
    @PostMapping
    @Transactional
    public ResponseEntity<FlightDataOutput> registerFlight(
            @Valid @RequestBody FlightDataInput dataInput, UriComponentsBuilder componentsBuilder) {

        var flight = flightService.registerFlight(dataInput);
        var uri = componentsBuilder.path("/flight/{id}")
                .buildAndExpand(flight.getId()).toUri();
        return ResponseEntity.created(uri).body(new FlightDataOutput(flight));
    }

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseEntity<Page<FlightSetOutput>> flightList(
            @PageableDefault(size = 10, sort = {"arrivalTime"}) Pageable pageable) {
        Page<FlightSetOutput> setFlight = flightService.flightList(pageable);

        return ResponseEntity.ok().body(setFlight);
    }

    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    public ResponseEntity<FlightDataOutput> findFlyById(@PathVariable Long id) {
        var fly = flightService.findFlyBYId(id);

        return ResponseEntity.ok().body(fly);
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<FlightDataOutput> changeFlightDateTime(
            @RequestBody @Valid FlightDataUpdateDateTime flightUpdateDateTime,
            @PathVariable Long id) {

        var flightUpdated = flightService.changeFlightDateTime(flightUpdateDateTime, id);
        return ResponseEntity.ok().body(new FlightDataOutput(flightUpdated));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public Map<String, String> cancelFlightById(@PathVariable Long id) {
        flightService.cancelFlightById(id);
        Map<String, String> message = new HashMap<>();
        message.put("Message: ", "FLY CANCELED");
        return message;
    }
}
