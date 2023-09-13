package com.br.rodrigo.jornadamilhas.domains.flightReservation.validations;

import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataInput;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ValidException;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationSoldOutTrue implements ValidationFlightReservation {
    @Autowired
    private FlightRepository flightRepository;

    public void validate(ReservationDataInput dataInput) {
        var flight = flightRepository.findById(dataInput.flight_id())
                .orElseThrow(() -> new DataNotFoundException("Flight not found."));
        if (flight.getSoldOut() == true) {
            throw new ValidException("Flight unavailable. Seats sold out.");
        }
    }
}
