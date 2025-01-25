package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import com.br.rodrigo.jornadamilhas.domains.flight.*;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.CancelReason;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.repositories.DestinationRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {


    @Mock
    private FlightRepository flightRepository;
    @Mock
    private DestinationRepository destinationRepository;
    @Mock
    private FlightReservationService flightReservationService;
    @Mock
    private FlightReservationRepository flightReservationRepository;
    @InjectMocks
    private FlightService flightService;
    @Captor
    private ArgumentCaptor<Flight> flightArgumentCaptor;
    @Mock
    private FlightDataInput dataInput;
    @Mock
    private Destination destination;
    private Flight flight;
    @Mock
    private Flight flightMock;
    @Captor
    private ArgumentCaptor<FlightReservation> registerFlightsCaptor;

    @BeforeEach
    void beforeEach() {
        flight = new Flight(null, LocalDateTime.now().minusHours(1), LocalDateTime.now(), 2, "Vasp",
                "Embraer", false,
                new Destination(null, null, null, "Petrolina", null, null, BigDecimal.valueOf(1000)), null);
    }


    @Test
    @DisplayName("Should Register Flight when validate pass")
    void registerFlight() {
        //Arrange
        given(destinationRepository.findById(dataInput.destination_id())).willReturn(Optional.of(destination));
        given(flightRepository.existsByDestinationIdAndDepartureTime(
                dataInput.destination_id(), dataInput.departureTime())).willReturn(false);
        //Act
        flightService.registerFlight(dataInput);
        //Assertion
        then(flightRepository).should().save(flightArgumentCaptor.capture());
        Flight capturedFlight = flightArgumentCaptor.getValue();
        assertEquals(dataInput.destination_id(), capturedFlight.getDestination().getId());
        assertEquals(dataInput.departureTime(), capturedFlight.getDepartureTime());

    }

    @Test
    @DisplayName("Should throws exception when destination is empty")
    void registerFlight_scenario02() {
        //Arrange
        given(destinationRepository.findById(dataInput.destination_id())).willReturn(Optional.empty());

        //Act && Assertion
        assertThrows(DataNotFoundException.class, () -> flightService.registerFlight(dataInput));
    }

    @Test
    @DisplayName("Should throws exception when destination and flight time departure already exists")
    void registerFlight_scenario03() {
        //Arrange
        given(destinationRepository.findById(dataInput.destination_id())).willReturn(Optional.of(destination));
        given(flightRepository.existsByDestinationIdAndDepartureTime(

                dataInput.destination_id(), dataInput.departureTime())).willReturn(true);

        //Act && Assertion
        assertThrows(ExistingDataException.class, () -> flightService.registerFlight(dataInput));
    }

    @Test
    @DisplayName("Should Return a flight list when validate pass")
    void flightList() {
        //Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("arrivalTime"));
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        Page<Flight> flightPage = new PageImpl<>(flights);
        given(flightRepository.findAllBySoldOutFalse(pageable)).willReturn(flightPage);
        //Act
        Page<FlightSetOutput> flightSetOutputs = flightService.flightList(pageable);
        //Assert
        assertNotNull(flightSetOutputs);
        assertEquals(1, flightSetOutputs.getSize());
        assertEquals("Petrolina", flightSetOutputs.getContent().get(0).destination());
        assertEquals("Vasp", flightSetOutputs.getContent().get(0).airline());

    }

    @Test
    @DisplayName("Should Return exception when validate not pass")
    void flightList_scenario01() {
        //Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("arrivalTime"));

        given(flightRepository.findAllBySoldOutFalse(pageable)).willReturn(Page.empty());
        //Act && Assert
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> flightService.flightList(pageable));
        assertEquals("No Flight was found.", exception.getMessage());
        verify(flightRepository).findAllBySoldOutFalse(pageable);

    }

    @Test
    @DisplayName("Should Return Flight Object when validate pass")
    void findFlyBYId_scenario01() {
        //Arrange
        Long id = 1L;
        given(flightRepository.findById(id)).willReturn(Optional.of(flight));
        //Act
        FlightDataOutput flightResult = flightService.findFlyBYId(id);
        //Assert
        assertNotNull(flightResult);
        assertFalse(flight.getSoldOut());
        assertEquals("Embraer", flightResult.aircraftModel());
        assertEquals("Petrolina", flightResult.destination());

    }

    @Test
    @DisplayName("Should Return Exception when Flight not exists")
    void findFlyBYId_scenario02() {
        //Arrange
        Long id = 1L;
        given(flightRepository.findById(id)).willReturn(Optional.empty());
        //Act && Assert
        assertThrows(DataNotFoundException.class, () -> flightService.findFlyBYId(id));
        //Assert
        assertFalse(flight.getSoldOut());
    }

    @Test
    @DisplayName("Should Return Exception when Flight sold out")
    void findFlyBYId_scenario03() {
        //Arrange
        Long id = 1L;
        given(flightRepository.findById(id)).willReturn(Optional.of(flightMock));
        given(flightMock.getSoldOut()).willReturn(true);
        //Act && Assert
        var dataException = assertThrows(DataNotFoundException.class, () -> flightService.findFlyBYId(id));
        //Assert
        assertTrue(flightMock.getSoldOut());
        assertEquals("Fly Sold out or Cancelled.", dataException.getMessage());
    }

    @Test
    @DisplayName("Should Update a flight Date when Flight Validate passes")
    void changeFlightDateTime_scenario01() {
        //Arrange
        Long id = 1L;
        LocalDateTime departureTime = LocalDateTime.now().minusDays(2);
        LocalDateTime arrivalTime = departureTime.plusHours(2);
        given(flightRepository.findById(id)).willReturn(Optional.of(flight));
        FlightDataUpdateDateTime flightDataUpdateDateTime = new FlightDataUpdateDateTime(departureTime, arrivalTime);
        //Act
        Flight flightResult = flightService.changeFlightDateTime(flightDataUpdateDateTime, id);
        //Assert;
        assertFalse(flight.getSoldOut());
        assertEquals(flightDataUpdateDateTime.arrivalTime(), flightResult.getArrivalTime());
        assertEquals(flightDataUpdateDateTime.departureTime(), flightResult.getDepartureTime());

    }

    @Test
    @DisplayName("Should Save a new date flight reservation when date is updated")
    void changeFlightDateTime_scenario02() {
        //Arrange
        Long id = 1L;
        LocalDateTime departureTime = LocalDateTime.now().minusDays(2);
        LocalDateTime arrivalTime = departureTime.plusHours(2);
        given(flightRepository.findById(id)).willReturn(Optional.of(flight));
        FlightDataUpdateDateTime flightDataUpdateDateTime = new FlightDataUpdateDateTime(departureTime, arrivalTime);
        FlightReservation flightReservation = new FlightReservation(null, new Client(), flight, flightDataUpdateDateTime.departureTime(),
                2, BigDecimal.valueOf(1000), CancelReason.FLIGHT_CANCELLED);
        given(flightReservationRepository.findAllByFlight(flight)).willReturn(List.of(flightReservation));

        //Act
        Flight flightResult = flightService.changeFlightDateTime(flightDataUpdateDateTime, id);
        //Assert;
        verify(flightReservationRepository, times(1)).save(registerFlightsCaptor.capture());
        FlightReservation capturedReservationFlights = registerFlightsCaptor.getValue();
        assertEquals(flightResult.getDepartureTime(), capturedReservationFlights.getFlightDate());
        assertEquals(departureTime, flightResult.getDepartureTime());
        assertEquals(arrivalTime, flightResult.getArrivalTime());
        assertEquals(CancelReason.FLIGHT_CANCELLED, capturedReservationFlights.getCancelReason());
        assertNotNull(capturedReservationFlights.getFlight());
        assertEquals(flight, capturedReservationFlights.getFlight());

    }

    @Test
    @DisplayName("Should Update a flight Date when method delayingFlight is called")
    void changeFlightDateTime_scenario03() {
        //Arrange
        Long id = 1L;
        LocalDateTime departureTime = LocalDateTime.now().minusDays(2);
        LocalDateTime arrivalTime = departureTime.plusHours(2);
        given(flightRepository.findById(id)).willReturn(Optional.of(flightMock));
        FlightDataUpdateDateTime flightDataUpdateDateTime = new FlightDataUpdateDateTime(departureTime, arrivalTime);
        //Act
        flightService.changeFlightDateTime(flightDataUpdateDateTime, id);
        //Assert;
        assertFalse(flightMock.getSoldOut());
        verify(flightMock).delayingFlight(flightDataUpdateDateTime);
    }

    @Test
    @DisplayName("Should cancel a flight when validate pass")
    void cancelFlightById() {
        //Arrange
        Long id = 1L;
        given(flightRepository.findById(id)).willReturn(Optional.of(flightMock));
        // Act
        flightService.cancelFlightById(id);
        //Assert
        verify(flightMock, times(1)).cancelFlight();
        verify(flightReservationService).cancelReservationsByFlight(flightMock);
        verify(flightRepository).save(flightMock);

    }
}