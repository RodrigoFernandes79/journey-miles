package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.destination.*;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.exceptions.ExistingDataException;
import com.br.rodrigo.jornadamilhas.repositories.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DestinationService {
    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private OpenApiService openApiService;

    public Destination createDestination(DestinationDataInput destinationDataInput) {
        var verifyDestination = destinationRepository
                .findByNameIgnoreCase(destinationDataInput.name());
        if (verifyDestination.isPresent()) {
            throw new ExistingDataException("Name Already Exists");
        }
        var destination = destinationRepository.save(new Destination(destinationDataInput));

        return destination;
    }

    public Page<ListDestinationDataOutput> listAllDestination(Pageable pageable) {
        Page<Destination> destination = destinationRepository.findAll(pageable);
        if (destination.isEmpty()) {
            throw new DataNotFoundException("No Destination was found");
        }
        Page<ListDestinationDataOutput> destinationList = destination
                .map(ListDestinationDataOutput::new);

        return destinationList;
    }

    public DestinationDataOutput findDestinationById(Long id) {
        var verifyDestination = destinationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Destination " + id + " not found."));

        var destinationOutput = new DestinationDataOutput(verifyDestination);

        return destinationOutput;
    }

    public Page<ListDestinationDataOutput> findDestinationByName(String name, Pageable pageable) {
        Page<Destination> verifyName = destinationRepository
                .findAllByNameIgnoreCaseContaining(name, pageable);
        if (verifyName.isEmpty()) {
            throw new DataNotFoundException("No destination was found.");
        }
        Page<ListDestinationDataOutput> destinationPage = verifyName.map(ListDestinationDataOutput::new);

        return destinationPage;
    }

    public DestinationDataOutputUpdate updateDestination(
            Long id, DestinationDataInputUpdate dataInputUpdate) {
        var verifyDestination = destinationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Destination " + id + " not found"));
        if (verifyDestination.getTextDescription() == null
                && dataInputUpdate.textDescription() == null) {

            verifyDestination
                    .setTextDescription(openApiService.generateTextDescription(verifyDestination.getName()));
        }
        verifyDestination.dataUpdateDestination(dataInputUpdate);

        return new DestinationDataOutputUpdate(verifyDestination);
    }

    public void deleteDestination(Long id) {
        var verifyDestination = destinationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Destination " + id + " not found."));

        destinationRepository.delete(verifyDestination);
    }
}


