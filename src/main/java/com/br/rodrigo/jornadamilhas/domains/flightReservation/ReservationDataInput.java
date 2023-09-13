package com.br.rodrigo.jornadamilhas.domains.flightReservation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationDataInput(
        @NotNull(message = "{client.required}")
        Long client_id,
        @NotNull(message = "{flight.required}")
        Long flight_id,
        @NotNull(message = "{number-seats.required}")
        @Min(value = 1, message = "{number-seats-min.required}")
        int numberOfSeats,
        LocalDateTime flightDate
) {
}
