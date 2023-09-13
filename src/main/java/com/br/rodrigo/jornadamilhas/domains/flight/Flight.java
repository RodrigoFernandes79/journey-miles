package com.br.rodrigo.jornadamilhas.domains.flight;

import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime departureTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime arrivalTime;
    private int totalSeats;
    private String airline;
    private String aircraftModel;
    private Boolean soldOut;
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private Set<FlightReservation> flightReservations = new HashSet<>();

    public Flight(FlightDataInput dataInput) {
        this.departureTime = dataInput.departureTime();
        this.arrivalTime = dataInput.arrivalTime();
        this.totalSeats = dataInput.totalSeats();
        this.airline = dataInput.airline();
        this.aircraftModel = dataInput.aircraftModel();
        this.soldOut = false;
    }

    public void cancelFlight() {
        this.soldOut = true;
    }

    public void delayingFlight(FlightDataUpdateDateTime data) {
        if (data.departureTime() != null) {
            this.departureTime = data.departureTime();
        }
        if (data.arrivalTime() != null) {
            this.arrivalTime = data.arrivalTime();
        }
    }
}

