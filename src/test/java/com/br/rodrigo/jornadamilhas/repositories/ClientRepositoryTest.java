package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.address.Address;
import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.integrationTests.testContainer.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
/* Garante que o banco de dados configurado no  Testcontainers seja usado nos testes do repository,
sem substituição automática por um banco em memória (como H2). */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private ClientRepository clientRepository;

    private Client client;
    private Address address;

    @BeforeEach
    void beforeEachMethod() {
        //Arrange / Given
        address = new Address("anu street", "any district", "34532348", "343",
                "403", "any city", "any State");
        client = new Client(null, "inaldinho", "email@email.com",
                "81995458654", "58696505000", "inaldinho.jpeg", "1312", null, address, null);
    }

    @Test
    @DisplayName("Should return object Client when find by email")
    void findByEmail() {
        //Arrange / given
        clientRepository.save(client);
        //Act / When
        Optional<Client> clientEntity = clientRepository.findByEmail(client.getEmail());
        //Assert / Then
        assertNotNull(clientEntity);
        assertEquals(client.getEmail(), clientEntity.get().getEmail());
    }

    @Test
    @DisplayName("Should return a client object when find by cpf")
    void findByCpf() {
        //Arrange / given
        clientRepository.save(client);
        //Act / When
        Optional<Client> clientEntity = clientRepository.findByCpf(client.getCpf());
        //Assert / Then
        assertNotNull(clientEntity);
        assertEquals(client.getCpf(), clientEntity.get().getCpf());
    }
}