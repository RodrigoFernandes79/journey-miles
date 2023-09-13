package com.br.rodrigo.jornadamilhas.domains.flightReservation.validations;

import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataInput;
import com.br.rodrigo.jornadamilhas.exceptions.ValidException;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidationThreeHoursBefore implements ValidationFlightReservation {
    @Autowired
    private FlightRepository flightRepository;

    public void validate(ReservationDataInput dataInput) {
        var flight = flightRepository.findById(dataInput.flight_id());

        var flightDate = flight.get().getDepartureTime();
        var now = dataInput.flightDate();
         now = LocalDateTime.now();
        var differenceInHours = Duration.between(now, flightDate).toHours();
        if (differenceInHours < 3) {
            throw new ValidException("Reservation can only be made 3 hours before departure time.");
        }

    }
}
