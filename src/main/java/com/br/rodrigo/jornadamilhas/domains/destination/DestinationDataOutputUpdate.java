package com.br.rodrigo.jornadamilhas.domains.destination;

import java.math.BigDecimal;

public record DestinationDataOutputUpdate(String name, String photo, BigDecimal price) {
    public DestinationDataOutputUpdate(Destination destination) {
        this(
                destination.getName(),
                destination.getPhoto(),
                BigDecimal.valueOf(destination.getPrice().doubleValue())
        );
    }
}
