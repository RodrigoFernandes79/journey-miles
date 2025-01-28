package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.address.Address;
import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.comments.Comment;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
/* Garante que o banco de dados configurado no  Testcontainers seja usado nos testes do repository,
sem substituição automática por um banco em memória (como H2). */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ClientRepository clientRepository;
    private Client client;
    private Address address;
    private Comment comment;

    @BeforeEach
    void BeforeEachMethod() {
        //Arrange / Given
        address = new Address("anu street", "any district", "34532348", "343",
                "403", "any city", "any State");
        client = new Client(null, "inaldinho", "email@email.com",
                "81995458654", "58696505000", "inaldinho.jpeg", "1312", null, address, null);
        comment = new Comment(null, "photo.jpg", client.getUsername(), "any comment", client);
    }

    @Test
    @DisplayName("Should return a page list comment when find by id client")
    void findCommentByClientId() {
        //arrange / Given
        clientRepository.save(client);
        commentRepository.save(comment);
        //Act / When
        Pageable pageable = PageRequest.of(0, 6, Sort.by("username"));
        Page<Comment> comentEntity = commentRepository.findCommentByClientId(comment.getId(), pageable);
        //Assert / Then
        assertNotNull(comentEntity);


    }
}