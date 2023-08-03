package com.br.rodrigo.jornadamilhas.domains.destination;

import java.math.BigDecimal;

public record ListDestinationDataOutput(Long id, String name, String photo, BigDecimal price) {
    public ListDestinationDataOutput(Destination destination) {
        this(
                destination.getId(),
                destination.getName(),
                destination.getPhoto(),
                BigDecimal.valueOf(destination.getPrice().doubleValue())
        );
    }
}
