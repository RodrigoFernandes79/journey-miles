package com.br.rodrigo.jornadamilhas.domains.flightReservation;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import com.br.rodrigo.jornadamilhas.domains.flight.Flight;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight_reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class FlightReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime flightDate;
    private int numberOfSeats;
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private CancelReason cancelReason;

    public void calculateTotalPrice() {
        if (flight != null && numberOfSeats > 0) {
            totalPrice = flight.getDestination().getPrice().multiply(BigDecimal.valueOf(numberOfSeats));
        } else {
            totalPrice = BigDecimal.ZERO;
        }
    }

    public void cancel(CancelReason cancelReason) {
        this.cancelReason = cancelReason;
    }
}
