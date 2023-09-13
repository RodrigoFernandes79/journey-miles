package com.br.rodrigo.jornadamilhas.domains.flightReservation.validations;

import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataInput;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationVerifyClient implements ValidationFlightReservation {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private FlightReservationRepository flightReservationRepository;


    public void validate(ReservationDataInput dataInput) {
        var client = clientRepository.findById(dataInput.client_id());
        var flight = flightRepository.findById(dataInput.flight_id());

        if (client.isEmpty()) {
            throw new DataNotFoundException("Client does not exist");
        }

        if (flight.isEmpty()) {
            throw new DataNotFoundException("Flight does not exist");
        }

    }
}