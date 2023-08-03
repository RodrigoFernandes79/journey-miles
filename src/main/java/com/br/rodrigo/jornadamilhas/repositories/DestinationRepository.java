package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Optional<Destination> findByNameIgnoreCase(String name);

    @Query(name = """
            "SELECT d FROM Destination d WHERE d.name LIKE :name%"
                       """)
    Page<Destination> findAllByNameIgnoreCaseContaining(@Param("name") String name, Pageable pageable);
}
