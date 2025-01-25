package com.br.rodrigo.jornadamilhas.controllers;


import com.br.rodrigo.jornadamilhas.domains.destination.*;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.repositories.DestinationRepository;
import com.br.rodrigo.jornadamilhas.services.DestinationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class DestinationControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private DestinationService destinationService;
    @MockBean
    private DestinationRepository destinationRepository;
    @Autowired
    private JacksonTester<DestinationDataInput> destinationDataInputJacksonTester;
    @Autowired
    private JacksonTester<DestinationDataOutputRegister> destinationDataOutputRegisterJacksonTester;
    @Autowired
    private JacksonTester<DestinationDataOutput> destinationDataOutputJacksonTester;
    @Autowired
    private JacksonTester<DestinationDataInputUpdate> destinationDataInputUpdateJacksonTester;
    @Autowired
    private JacksonTester<DestinationDataOutputUpdate> destinationDataOutputUpdateJacksonTester;
    @Autowired
    private JacksonTester<Page<ListDestinationDataOutput>> listDestinationDataOutputJacksonTester;

    DestinationControllerTest() {
    }


    @Test
    @DisplayName(value = "createDestination: Should return http 201 when information´s are valid")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void createDestination_scenario_1() throws Exception {
        //Arrange
        Destination destination = new Destination();
        destination.setId(1L);
        destination.setName("Paris");
        destination.setPrice(BigDecimal.valueOf(500.00));
        when(destinationService.createDestination(any())).thenReturn(destination);
        //Act
        var result = mvc.perform(post("/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(destinationDataInputJacksonTester
                                .write(new DestinationDataInput(
                                        destination.getName(), null, destination.getPrice())
                                ).getJson())
                )
                .andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonResponse = result.getContentAsString();
        var jsonExpected = destinationDataOutputRegisterJacksonTester.write(new DestinationDataOutputRegister(destination)).getJson();
        assertThat(jsonResponse).isEqualTo(jsonExpected);
    }

    @Test
    @DisplayName(value = "createDestination: Should return http 400 when information´s are invalids")
    @WithMockUser
    void createDestination_scenario_2() throws Exception {
        //Act
        var result = mvc.perform(post("/destinations"))
                .andReturn().getResponse();
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("listAllDestination: Should return  http 200 when information´s are valid")
    @WithMockUser
    void listAllDestination_scenario_1() throws Exception {
        //Arrange
        Pageable pageable = PageRequest.of(0, 6, Sort.by("name"));
        var destination1 = new Destination(1L, null, null, "Paris", null,
                null, BigDecimal.valueOf(500.00));
        var destination2 = new Destination(2L, null, null,
                "Paraguay", null, null, BigDecimal.valueOf(350.00));
        List<Destination> destinationList = Arrays.asList(destination1, destination2);
        Page<Destination> destinationPage = new PageImpl<>(destinationList);
        when(destinationRepository.findAll(pageable))
                .thenReturn(destinationPage);
        when(destinationService.listAllDestination(pageable))
                .thenReturn(destinationPage.map(ListDestinationDataOutput::new));
        //Act
        var result = mvc.perform(get("/destinations", pageable)

        ).andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonResponse = result.getContentAsString();
        var jsonExpected = listDestinationDataOutputJacksonTester
                .write(destinationPage.map(ListDestinationDataOutput::new)).getJson();

        JSONAssert.assertEquals(jsonExpected, jsonResponse, false);

    }

    @Test
    @DisplayName("listAllDestination: Should return  http 404 when destinations list are empty")
    @WithMockUser
    void listAllDestination_scenario_2() throws Exception {
        //Arrange
        Pageable pageable = PageRequest.of(0, 6, Sort.by("name"));
        Page<Destination> destinationPage = new PageImpl<>(Collections.emptyList());
        when(destinationRepository.findAll(pageable))
                .thenReturn(destinationPage);
        when(destinationService.listAllDestination(any()))
                .thenThrow(DataNotFoundException.class);
        //Act
        var result = mvc.perform(get("/destinations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "6")
                .param("sort", "name")
        ).andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName(value = "findDestinationById: Should return http 200 when information´s are valid")
    @WithMockUser
    void findDestinationById_scenario_1() throws Exception {
        //Arrange
        Long id = 1L;
        Destination destination = new Destination();
        destination.setId(id);
        destination.setName("Paris");
        destination.setPrice(BigDecimal.valueOf(500.00));
        when(destinationService.findDestinationById(anyLong()))
                .thenReturn(new DestinationDataOutput(destination));
        //Act
        var result = mvc.perform(get("/destinations/{id}", id))
                .andReturn()
                .getResponse();
        //Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonResponse = result.getContentAsString();
        var jsonExpected = destinationDataOutputJacksonTester
                .write(new DestinationDataOutput(destination)).getJson();
        assertThat(jsonResponse).isEqualTo(jsonExpected);
    }

    @Test
    @DisplayName(value = "findDestinationById: Should return http 400 when id is invalid")
    @WithMockUser
    void findDestinationById_scenario_2() throws Exception {
        //Arrange
        Long id = null;
        //Act
        var result = mvc.perform(get("/destinations/{id}", id))
                .andReturn()
                .getResponse();
        //Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @Test
    @DisplayName("findDestinationByName: Should return http 200 when information´s are valid")
    @WithMockUser
    void findDestinationByName_scenario_1() throws Exception {
        //Arrange
        String name = "Paris";
        Pageable pageable = PageRequest.of(0, 6, Sort.by("price"));
        var destination1 = new Destination(1L, null,
                "Paris", null, null, null, BigDecimal.valueOf(500.00));
        var destination2 = new Destination(2L, null,
                "Paraguay", null, null, null, BigDecimal.valueOf(350.00));
        List<Destination> destinationList = Arrays.asList(destination1, destination2);
        Page<Destination> destinationPage = new PageImpl<>(destinationList, pageable, 2L);
        when(destinationRepository.findAllByNameIgnoreCaseContaining(name, pageable))
                .thenReturn(destinationPage);
        when(destinationService.findDestinationByName(any(), any()))
                .thenReturn((destinationPage.map(ListDestinationDataOutput::new)));
        //Act
        var result = mvc.perform(get("/destinations/search", pageable)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)

        ).andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        var jsonResponse = result.getContentAsString();
        var jsonExpected = listDestinationDataOutputJacksonTester
                .write(destinationPage.map(ListDestinationDataOutput::new)).getJson();
        JSONAssert.assertEquals(jsonExpected, jsonResponse, true);
    }

    @Test
    @DisplayName("findDestinationByName: Should return http 200 when information´s are valid")
    @WithMockUser
    void findDestinationByName_scenario_2() throws Exception {
        //Arrange
        String name = "Paris";
        Pageable pageable = PageRequest.of(0, 6, Sort.by("price"));
        Page<Destination> destinationPage = new PageImpl<>(Collections.emptyList());
        when(destinationRepository.findAllByNameIgnoreCaseContaining(name, pageable))
                .thenReturn(destinationPage);
        when(destinationService.findDestinationByName(any(), any()))
                .thenThrow(DataNotFoundException.class);
        //Act
        var result = mvc.perform(get("/destinations/search", pageable)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)

        ).andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("updateDestination: Should return http 200 when information´s are valid")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void updateDestination_scenario_1() throws Exception {
        //Arrange
        Long id = 1L;
        DestinationDataInputUpdate dataInputUpdate = new DestinationDataInputUpdate(
                "paris.jpg", "paris2.jpeg", "Welcome to Paris", null, BigDecimal.valueOf(500.00));
        var destination = new Destination();
        destination.setId(id);
        destination.setName("David");
        destination.dataUpdateDestination(dataInputUpdate);
        when(destinationRepository.findById(anyLong())).thenReturn(Optional.of(destination));
        when(destinationService.updateDestination(any(), any()))
                .thenReturn(new DestinationDataOutputUpdate(destination));
        //Act
        var result = mvc.perform(
                        patch("/destinations/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(destinationDataInputUpdateJacksonTester.
                                        write(new DestinationDataInputUpdate(destination.getPhoto(),
                                                destination.getPhoto2(), destination.getMetaDescription(),
                                                destination.getTextDescription(), destination.getPrice())).getJson())
                )
                .andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

        var responseJson = result.getContentAsString();
        var expectedJson = destinationDataOutputUpdateJacksonTester
                .write(new DestinationDataOutputUpdate(destination)).getJson();
        assertThat(responseJson).isEqualTo(expectedJson);

    }

    @Test
    @DisplayName("updateDestination: Should return http 400 when information´s are invalid")
    @WithMockUser
    void updateDestination_scenario_2() throws Exception {
        //Arrange
        Long id = 1L;
        var result = mvc.perform(
                        patch("/destinations/{id}", id)
                )
                .andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("updateDestination: Should return http 404 when id is invalid")
    @WithMockUser
    void updateDestination_scenario_3() throws Exception {
        //Arrange
        Long id = null;
        when(destinationRepository.findById(anyLong())).thenThrow(new DataNotFoundException("id not found"));
        var result = mvc.perform(
                        patch("/destinations/{id}", id))
                .andReturn().getResponse();
        //Asserts
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deleteDestination: Should return http 404 when destination not found")
    @WithMockUser
    void deleteDestination_scenario_01() throws Exception {
        //Arrange
        Long id = null;
        var destination = new Destination();
        when(destinationRepository.findById(anyLong())).thenReturn(Optional.of(destination));
        //Act
        var result = mvc.perform(delete("/destinations/{id}", id)
        ).andReturn().getResponse();

        //Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deleteDestination: Should return http 200 when information´s are valid")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void deleteDestination_scenario_02() throws Exception {
        //Arrange
        Long id = 1L;
        var destination = new Destination();
        destination.setId(id);
        destination.setName("Paris");
        destination.setPrice(BigDecimal.valueOf(500.00));
        when(destinationRepository.findById(anyLong())).thenReturn(Optional.of(destination));
        //Act
        var result = mvc.perform(delete("/destinations/{id}", id)
        ).andReturn().getResponse();

        //Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}