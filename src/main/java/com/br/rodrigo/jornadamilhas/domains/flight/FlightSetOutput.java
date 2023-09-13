package com.br.rodrigo.jornadamilhas.domains.flight;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record FlightSetOutput(
        Long id,
        String destination,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime departureTime,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime arrivalTime,
        int totalSeats,
        String airline,
        String aircraftModel


) {
    public FlightSetOutput(Flight flight) {
        this(flight.getId(), flight.getDestination().getName(),
                flight.getDepartureTime(), flight.getArrivalTime(),
                flight.getTotalSeats(), flight.getAirline(), flight.getAircraftModel());
    }
}
