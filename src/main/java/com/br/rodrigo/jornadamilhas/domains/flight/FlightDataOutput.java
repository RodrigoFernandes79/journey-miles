package com.br.rodrigo.jornadamilhas.domains.flight;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record FlightDataOutput(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime departureTime,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime arrivalTime,
        String destination,
        int totalSeats,
        String airline,
        String aircraftModel) {

    public FlightDataOutput(Flight flight) {
        this(flight.getId(), flight.getDepartureTime(), flight.getArrivalTime(),
                flight.getDestination().getName(), flight.getTotalSeats(),
                flight.getAirline(), flight.getAircraftModel());
    }
}
