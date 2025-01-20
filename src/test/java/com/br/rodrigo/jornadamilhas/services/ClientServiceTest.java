package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInput;
import com.br.rodrigo.jornadamilhas.domains.client.ClientDataInputUpdate;
import com.br.rodrigo.jornadamilhas.domains.client.ListClientDataOutput;
import com.br.rodrigo.jornadamilhas.domains.user.User;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.exceptions.PasswordNotEqualsException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UserRepository userRepository;
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
    @Captor
    private ArgumentCaptor<ClientDataInputUpdate> clientDataInputUpdateArgumentCaptor;
    @Mock
    private User user;


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

    @Test
    @DisplayName("Should return updated objects when validate passes")
    void updateClient_scenario01() {
        //Arrange /Given
        Long id = 1L;
        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        //Act / When
        clientService.updateClient(id, new ClientDataInputUpdate("95455945049", "newphoto.jpg", null));
        //Arrange /Then
        then(client).should().dataUpdateClient(clientDataInputUpdateArgumentCaptor.capture());
        assertEquals("95455945049", clientDataInputUpdateArgumentCaptor.getValue().phoneNumber());
        assertEquals("newphoto.jpg", clientDataInputUpdateArgumentCaptor.getValue().photo());
        assertNull(clientDataInputUpdateArgumentCaptor.getValue().dataAddress());
    }

    @Test
    @DisplayName("Should return exception  when validate not passes")
    void updateClient_scenario02() {
        //Arrange /Given
        Long id = 1L;
        given(clientRepository.findById(id)).willReturn(Optional.empty());
        //Act / When && Arrange /Then
        assertThrows(DataNotFoundException.class, () -> clientService.updateClient(id,
                new ClientDataInputUpdate("95455945049", "newphoto.jpg", null)));

    }

    @Test
    @DisplayName("Should return Object Client  when validate pass")
    void findClientById_scenario01() {
        //Arrange / Given
        Long id = 1L;
        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        //Act / When
        Client response = clientService.findClientById(id);
        //Arrange / Then
        assertEquals(client.getPhoneNumber(), response.getPhoneNumber());
        assertNotNull(response);
        assertEquals(client.getCpf(), response.getCpf());
    }

    @Test
    @DisplayName("Should return exception  when validate  not pass")
    void findClientById_scenario02() {
        //Arrange / Given
        Long id = 1L;
        given(clientRepository.findById(id)).willReturn(Optional.empty());
        //Act / When && Arrange / Then
        assertThrows(DataNotFoundException.class, () -> clientService.findClientById(id));

    }

    @Test
    @DisplayName("Should delete client when validate pass")
    void deleteClientById_scenario01() {
        //Arrange / Given
        Long id = 1L;
        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        given(userRepository.findByEmail(client.getEmail())).willReturn(user);
        //Act / When
        clientService.deleteClientById(id);
        //Assertion / Then
        then(clientRepository).should().delete(client);
        verify(clientRepository, times(1)).delete(client);
        then(userRepository).should().delete(user);
        verify(userRepository, times(1)).delete(user);
        verify(client, never()).getUsername();
        verify(user, never()).getPassword();

    }

}