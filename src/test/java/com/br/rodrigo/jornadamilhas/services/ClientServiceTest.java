package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInput;
import com.br.rodrigo.jornadamilhas.domains.user.User;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import com.br.rodrigo.jornadamilhas.repositories.CommentRepository;
import com.br.rodrigo.jornadamilhas.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ClientService clientService;
    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;
    @Mock
    private ClientDataInput clientDataInput;

    @Test
    @DisplayName("Should Return a client when validate pass")
    void createClient() {
        //Arrange / Given
        given(clientRepository.findByEmail(clientDataInput.email())).willReturn(Optional.empty());
        given(clientRepository.findByCpf(clientDataInput.cpf())).willReturn(Optional.empty());
        given(clientDataInput.password()).willReturn("validPassword");
        given(clientDataInput.repeatPassword()).willReturn("validPassword");
        var encodedPassword = "encdedPassword";
        given(passwordEncoder.encode("validPassword")).willReturn(encodedPassword);
        //Act / when
        clientService.createClient(clientDataInput);
        //Assertion / then
        then(clientRepository).should().save(clientArgumentCaptor.capture());
        assertEquals(encodedPassword,clientArgumentCaptor.getValue().getPassword());
        verify(userRepository).save(new User(clientDataInput));
        

    }
}