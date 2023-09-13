package com.br.rodrigo.jornadamilhas.domains.flightReservation;

import jakarta.validation.constraints.NotNull;

public record ReservationDataCancel(
        @NotNull
        Long reservationId,
        @NotNull
        CancelReason cancelReason
) {
}
