package com.br.rodrigo.jornadamilhas.domains.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record FlightDataInput(
        @NotNull(message = "{departure-time.required}")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Future(message = "{future-departure.required}")
        LocalDateTime departureTime,
        @NotNull(message = "{arrival-time.required}")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Future(message = "{future-arrival.required}")
        LocalDateTime arrivalTime,
        @NotNull(message = "{destination.required}")
        Long destination_id,
        @NotNull(message = "{total-seats.required}")
        @Min(value = 1, message = "{total-seats-min.required}")
        int totalSeats,
        @NotEmpty(message = "{airline.required}")
        String airline,
        @NotEmpty(message = "{aircraft-model.required}")
        String aircraftModel


) {
}
