package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import com.br.rodrigo.jornadamilhas.integrationTests.testContainer.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
/* Garante que o banco de dados configurado no  Testcontainers seja usado nos testes do repository,
sem substituição automática por um banco em memória (como H2). */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DestinationRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private DestinationRepository destinationRepository;
    private Destination destination;

    @BeforeEach
    void BeforeEachMethod() {
        //Arrange / Given
        destination = new Destination(null, "photo1.jpg", "photo2.jpg",
                "any place", "any metaDescription",
                "any text Description", BigDecimal.valueOf(1000.00));

    }

    @Test
    @DisplayName("Should Return Destination by destination name ignoring case")
    void findByNameIgnoreCase() {
        //Arrange / Given
        destinationRepository.save(destination);
        //Act / when
        Optional<Destination> destinationEntity = destinationRepository.findByNameIgnoreCase("ANy Place");
        //Assert / Then
        assertNotNull(destinationEntity.get());
        assertEquals(destination.getName(), destinationEntity.get().getName());

    }

    @Test
    @DisplayName("Should Return Destination List by destination name ignoring case")
    void findAllByNameIgnoreCaseContaining() {
        //Arrange / Given
        destinationRepository.save(destination);
        Pageable pageable = PageRequest.of(0, 6, Sort.by("price"));
        //Act / when
        Page<Destination> destinationEntity = destinationRepository.findAllByNameIgnoreCaseContaining("AN", pageable);
        //Assert / Then
        assertNotNull(destinationEntity.get());
        assertEquals(1, destinationEntity.getContent().size());
        assertEquals(destination.getName(), destinationEntity.getContent().get(0).getName());

    }
}