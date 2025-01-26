package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.address.Address;
import com.br.rodrigo.jornadamilhas.domains.address.DataAddress;
import com.br.rodrigo.jornadamilhas.domains.client.*;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;

    @Autowired
    private JacksonTester<ClientDataInput> clientDataInputJacksonTester;
    @Autowired
    private JacksonTester<ClientDataOutput> clientDataOutputJacksonTester;
    @Autowired
    private JacksonTester<ClientDataInputUpdate> clientDataInputUpdateJacksonTester;
    @Autowired
    private JacksonTester<UpdateClientDataOutput> updateClientDataOutputJacksonTester;
    private Client client;

    @BeforeEach()
    void beforeEachMethod() {
        client = new Client(null, "James", "Green", "97343456956", "05786391060", "james.jpg", "R3er5gh70!",
                null,
                new Address("one street", "one district",
                        "545678987", "333", "102", "one city", "one state"),
                null);
    }

    @Test
    @DisplayName("Should Return 201 created http method when create a client")
    @WithMockUser
    void createClient_Scenario01() throws Exception {
        //Arrange
        given(clientService.createClient(any(ClientDataInput.class))).willReturn(client);
        //Act / when
        var response = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDataInputJacksonTester
                                .write(
                                        new ClientDataInput("James",
                                                "05786391060",
                                                "james@email.com",
                                                "R3er5gh70!",
                                                "R3er5gh70!"))
                                .getJson()))
                .andReturn()
                .getResponse();
        //Assertions / then
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        var expectedResponse = clientDataOutputJacksonTester.write(new ClientDataOutput(client)).getJson();
        assertEquals(response.getContentAsString(), expectedResponse);
    }

    @Test
    @DisplayName("Should Return 400 BAD_REQUEST http method when client not set")
    @WithMockUser
    void createClient_Scenario02() throws Exception {
        //Arrange
        String client = "{}";
        //Act / when
        var response = mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(client))
                .andReturn()
                .getResponse();
        //Assertions / then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should Return 200 OK http method when return client list")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void listAllClients_Scenario01() throws Exception {
        //Arrange
        List<ListClientDataOutput> listClientDataOutputs = new ArrayList<>();
        ListClientDataOutput clientDataOutput = new ListClientDataOutput(client);
        listClientDataOutputs.add(clientDataOutput);

        given(clientService.listAllClients()).willReturn(listClientDataOutputs);
        //Act
        var response = mockMvc.perform(get("/clients"));
        //Assert
        response
                .andExpect(status().isOk()).andReturn().getResponse();

    }

    @Test
    @DisplayName("Should Return 200 OK http method when return an updated client")
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void updateClient_scenario01() throws Exception {
        //arrange
        Long id = 1L;
        ClientDataInputUpdate clientDataInputUpdate = new ClientDataInputUpdate("83222394931",
                "anotherphoto.jpg",
                new DataAddress("another avenue", "another district", "56403584", "444", "903", "another city", "another state"));
        given(clientService.findClientById(id)).willReturn(client);
        given(clientService.updateClient(id, clientDataInputUpdate)).willReturn(client);
        //Act
        var result = mockMvc.perform(patch("/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDataInputUpdateJacksonTester.write(clientDataInputUpdate).getJson())
                )
                //Assert
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var resultAtual = result.getContentAsString();
        var resultExpected = updateClientDataOutputJacksonTester.write(new UpdateClientDataOutput(client)).getJson();
        assertEquals(resultExpected, resultAtual);

    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Should Return 404 NOT FOUND http method when id not exist")
    void updateClient_scenario02() throws Exception {
        //arrange
        Long id = 1L;
        ClientDataInputUpdate clientDataInputUpdate = new ClientDataInputUpdate(
                "83222394931",
                "anotherphoto.jpg",
                new DataAddress(
                        "another avenue",
                        "another district",
                        "56403584",
                        "444",
                        "903",
                        "another city",
                        "another state"));
        given(clientService.updateClient(id, clientDataInputUpdate)).willThrow(DataNotFoundException.class);
        //Act
        var result = mockMvc.perform(patch("/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientDataInputUpdateJacksonTester.write(clientDataInputUpdate).getJson())
                )
                //Assert
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Should Return a status 200 OK http method when find client by id")
    void findClientById() throws Exception {
        //Arrange
        Long id = 1L;
        given(clientService.findClientById(id)).willReturn(client);
        given(clientService.findClientById(id)).willReturn(client);
        //act
        var response = mockMvc.perform(get("/clients/{id}", id))
                .andReturn()
                .getResponse();
        //assert
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        var resultAtual = response.getContentAsString();
        var resultExpected = updateClientDataOutputJacksonTester.write(new UpdateClientDataOutput(client)).getJson();
        assertEquals(resultExpected, resultAtual);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Should Return 200 OK http method when delete a client")
    void deleteClientById() throws Exception {
        //arrange
        Long id = 1L;
        Map<String, String> message = new HashMap<>();
        message.put("Message", "Client deleted successfully");
        given(clientService.findClientById(id)).willReturn(client);
        willDoNothing().given(clientService).deleteClientById(id);
        //Act
        mockMvc.perform(delete("/clients/{id}", id))
                .andExpect(status().isOk());
        assertEquals("[Client deleted successfully]", message.values().toString());
    }
}