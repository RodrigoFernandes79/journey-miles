package com.br.rodrigo.jornadamilhas.domains.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FlightDataUpdateDateTime(
        @NotNull(message = "{departure-time.required}")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Future(message = "{future-departure.required}")
        LocalDateTime departureTime,
        @NotNull(message = "{arrival-time.required}")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Future(message = "{future-arrival.required}")
        LocalDateTime arrivalTime
) {
}
