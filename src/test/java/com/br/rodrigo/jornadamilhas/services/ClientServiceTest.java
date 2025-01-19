package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInput;
import com.br.rodrigo.jornadamilhas.domains.client.ListClientDataOutput;
import com.br.rodrigo.jornadamilhas.domains.user.User;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.exceptions.PasswordNotEqualsException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import com.br.rodrigo.jornadamilhas.repositories.CommentRepository;
import com.br.rodrigo.jornadamilhas.repositories.UserRepository;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private Client client;


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
        assertEquals(encodedPassword, clientArgumentCaptor.getValue().getPassword());
        verify(userRepository).save(new User(clientDataInput));


    }

    @Test
    @DisplayName("Should Return exception when email or cpf exists")
    void createClient_scenario01() {
        //Arrange / Given
        given(clientRepository.findByEmail(clientDataInput.email())).willReturn(Optional.of(client));
        given(clientRepository.findByCpf(clientDataInput.cpf())).willReturn(Optional.of(client));
        //Act / when && Assertion / then
        assertThrows(ExistingDataException.class, () -> clientService.createClient(clientDataInput));

    }

    @Test
    @DisplayName("Should Return exception when password is different than repeat password")
    void createClient_scenario02() {
        //Arrange / Given
        given(clientRepository.findByEmail(clientDataInput.email())).willReturn(Optional.empty());
        given(clientRepository.findByCpf(clientDataInput.cpf())).willReturn(Optional.empty());
        var password = "any password";
        var wrongPassword = "other password";
        given(clientDataInput.password()).willReturn(password);
        given(clientDataInput.repeatPassword()).willReturn(wrongPassword);
        //Act / when && Assertion / then
        assertThrows(PasswordNotEqualsException.class, () -> clientService.createClient(clientDataInput));

    }

    @Test
    @DisplayName("Should return Client List when validate pass")
    void listAllClients_scenario01() {
        //Arrange / Given
        List<Client> clientList = new ArrayList<>();
        clientList.add(client);
        given(clientRepository.findAll()).willReturn(clientList);
        List<ListClientDataOutput> dataOutputList = clientList.stream()
                .map(ListClientDataOutput::new).collect(Collectors.toList());

        //Act / when
        List<ListClientDataOutput> result = clientService.listAllClients();
        // Assertion / then
        assertEquals(1, result.size());
        assertEquals(clientList.size(), result.size());
        assertEquals(clientList.get(0).getEmail(), result.get(0).email());
        assertEquals(dataOutputList.get(0).username(), result.get(0).username());
    }

    @Test
    @DisplayName("Should return exception when validate not pass")
    void listAllClients_scenario02() {
        //Arrange / Given
        given(clientRepository.findAll()).willReturn(Collections.emptyList());

        //Act / when && Assertion / then
        assertThrows(DataNotFoundException.class, () -> clientService.listAllClients());
    }
}