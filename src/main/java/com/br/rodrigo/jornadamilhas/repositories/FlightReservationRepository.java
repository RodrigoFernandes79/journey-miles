package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.flight.Flight;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Long> {

    List<FlightReservation> findAllByFlight(Flight flight);
}
