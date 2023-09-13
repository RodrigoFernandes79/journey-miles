package com.br.rodrigo.jornadamilhas.controllers;


import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataCancel;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataInput;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataOutput;
import com.br.rodrigo.jornadamilhas.services.FlightReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/reservations")
public class FlightReservationController {

    @Autowired
    private FlightReservationService flightReservationService;

    @Secured("ROLE_USER")
    @PostMapping
    @Transactional
    public ResponseEntity<ReservationDataOutput> insertReservation(
            @RequestBody @Valid ReservationDataInput reservationDataInput,
            UriComponentsBuilder builder) {
        var reservation = flightReservationService.insertReservation(reservationDataInput);
        var uri = builder.path("/reservations/{id}")
                .buildAndExpand(reservation.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReservationDataOutput(reservation));

    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> cancelFlight(@Valid @RequestBody ReservationDataCancel dataCancel) {
        flightReservationService.cancelFlight(dataCancel);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
