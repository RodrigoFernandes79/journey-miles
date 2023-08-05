package com.br.rodrigo.jornadamilhas.domains.destination;

import java.math.BigDecimal;

public record DestinationDataOutputUpdate(
        String name,
        String photo_1,
        String photo_2,
        String metaDescription,
        String textDescription,
        BigDecimal price) {
    public DestinationDataOutputUpdate(Destination destination) {
        this(
                destination.getName(),
                destination.getPhoto(),
                destination.getPhoto2(),
                destination.getMetaDescription(),
                destination.getTextDescription(),
                BigDecimal.valueOf(destination.getPrice().doubleValue())
        );
    }
}
