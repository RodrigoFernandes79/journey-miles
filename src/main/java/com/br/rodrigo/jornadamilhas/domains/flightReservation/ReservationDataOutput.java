package com.br.rodrigo.jornadamilhas.domains.flightReservation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationDataOutput(Long id,
                                    String username,
                                    String destination,
                                    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
                                    LocalDateTime flightDate,
                                    BigDecimal destinationPrice,
                                    int numberOfSeats,
                                    BigDecimal totalPrice) {

    public ReservationDataOutput(FlightReservation flightReservation) {
        this(flightReservation.getId(), flightReservation.getClient().getUsername(),
                flightReservation.getFlight().getDestination().getName(),
                flightReservation.getFlight().getDepartureTime(), flightReservation.getFlight().getDestination().getPrice(),
                flightReservation.getNumberOfSeats(), flightReservation.getTotalPrice());
    }
}
