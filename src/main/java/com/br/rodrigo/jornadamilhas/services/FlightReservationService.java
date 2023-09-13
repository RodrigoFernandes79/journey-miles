package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.flight.Flight;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.CancelReason;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.FlightReservation;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataCancel;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.ReservationDataInput;
import com.br.rodrigo.jornadamilhas.domains.flightReservation.validations.ValidationFlightReservation;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ValidException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightRepository;
import com.br.rodrigo.jornadamilhas.repositories.FlightReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightReservationService {
    @Autowired
    private FlightReservationRepository flightReservationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private List<ValidationFlightReservation> validations;

    public FlightReservation insertReservation(ReservationDataInput reservationDataInput) {
        validations.forEach(v -> v.validate(reservationDataInput));

        var client = clientRepository.findById(reservationDataInput.client_id()).get();
        var flight = flightRepository.findById(reservationDataInput.flight_id()).get();

        var reservation = new FlightReservation();
        reservation.setClient(client);
        reservation.setFlight(flight);
        reservation.setFlightDate(flight.getDepartureTime());

        reservation.setNumberOfSeats(reservationDataInput.numberOfSeats());
        flight.setTotalSeats(flight.getTotalSeats() - reservation.getNumberOfSeats());
        flightRepository.save(flight);
        if (flight.getTotalSeats() == 0) {
            flight.setSoldOut(true);
            flightRepository.save(flight);
        }
        reservation.calculateTotalPrice();


        return flightReservationRepository.save(reservation);

    }

    public void cancelFlight(ReservationDataCancel dataCancel) {
        var reservation = flightReservationRepository.findById(dataCancel.reservationId())
                .orElseThrow(() -> new DataNotFoundException("Reservation not found."));
        var reservationDate = reservation.getFlight().getDepartureTime();
        var cancelDate = LocalDateTime.now();
        var differenceInHours = Duration.between(cancelDate, reservationDate).toHours();

        if (differenceInHours < 24) {
            throw new ValidException("Flight cancellation can only be done before 24 hours before departure time");
        }
        var totalSeats = reservation.getFlight().getTotalSeats();
        reservation.getFlight().setTotalSeats(totalSeats + reservation.getNumberOfSeats());
        if (totalSeats > 0) {
            reservation.getFlight().setSoldOut(false);
        }
        reservation.cancel(dataCancel.cancelReason());
    }

    public void cancelReservationsByFlight(Flight flight) {
        List<FlightReservation> reservations = flightReservationRepository.findAllByFlight(flight);

        for (FlightReservation reservation : reservations) {
            reservation.cancel(CancelReason.FLIGHT_CANCELLED);
        }

        flightReservationRepository.saveAll(reservations);
    }
}

