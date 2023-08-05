package com.br.rodrigo.jornadamilhas.domains.destination;

import java.math.BigDecimal;

public record DestinationDataOutputRegister(
        Long id,
        String name,
        String photo,
        BigDecimal price
) {
    public DestinationDataOutputRegister(Destination destination) {
        this(
                destination.getId(),
                destination.getName(),
                destination.getPhoto(),
                BigDecimal.valueOf(destination.getPrice().doubleValue()));
    }
}
