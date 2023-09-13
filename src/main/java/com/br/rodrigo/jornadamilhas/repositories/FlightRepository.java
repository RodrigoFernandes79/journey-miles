package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.flight.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    Boolean existsByDestinationIdAndDepartureTime(Long aLong, LocalDateTime localDateTime);

    Page<Flight> findAllBySoldOutFalse(Pageable pageable);

}
