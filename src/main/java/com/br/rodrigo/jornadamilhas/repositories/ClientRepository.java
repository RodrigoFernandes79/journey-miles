package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByCpf(String cpf);
}
