package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.flight.*;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.repositories.DestinationRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private FlightReservationService flightReservationService;
    @Autowired
    private FlightReservationRepository flightReservationRepository;

    public Flight registerFlight(FlightDataInput dataInput) {
        var destination = destinationRepository.findById(dataInput.destination_id())
                .orElseThrow(() -> new DataNotFoundException("Destination not found"));
        var flight = flightRepository.existsByDestinationIdAndDepartureTime(
                dataInput.destination_id(), dataInput.departureTime());
        if (flight == true) {
            throw new ExistingDataException("There´s already a flight with the same destination and departure time.");
        }
        var flightObj = new Flight(dataInput);
        flightObj.setDestination(destination);

        return flightRepository.save(flightObj);
    }

    public Page<FlightSetOutput> flightList(Pageable pageable) {
        Page<Flight> flights = flightRepository.findAllBySoldOutFalse(pageable);
        if (flights.isEmpty()) {
            throw new DataNotFoundException("No Flight was found.");
        }
        Page<FlightSetOutput> flightSetOutputs = flights.map(FlightSetOutput::new);

        return flightSetOutputs;
    }

    public FlightDataOutput findFlyBYId(Long id) {
        var flightEntity = flightRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flight not exists."));
        if (flightEntity.getSoldOut() == true) {
            throw new DataNotFoundException("Fly Sold out or Cancelled.");
        }
        return new FlightDataOutput(flightEntity);
    }

    public Flight changeFlightDateTime(FlightDataUpdateDateTime flightUpdateDateTime, Long id) {
        var flight = flightRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flight not found."));
        if (flight.getSoldOut() == true) {
            throw new DataNotFoundException("Fly Sold out or Cancelled.");
        }
        flight.delayingFlight(flightUpdateDateTime);
        var registerFlights = flightReservationRepository.findAllByFlight(flight);
        for (FlightReservation reservation : registerFlights) {
            reservation.setFlightDate(flightUpdateDateTime.departureTime());
        }
        return flight;
    }

    public void cancelFlightById(Long id) {
        var flight = flightRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flight not exists."));
        if (flight.getSoldOut() == true) {
            throw new DataNotFoundException("Fly´s already Sold out or Cancelled.");
        }
        flight.cancelFlight();
        flightRepository.save(flight);

        flightReservationService.cancelReservationsByFlight(flight);

    }
}
