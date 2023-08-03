package com.br.rodrigo.jornadamilhas.domains.destination;

import java.math.BigDecimal;

public record DestinationDataOutput(String name, String photo, BigDecimal price) {
    public DestinationDataOutput(Destination destination) {
        this(
                destination.getName(),
                destination.getPhoto(),
                BigDecimal.valueOf(destination.getPrice().doubleValue())
        );

    }
}
