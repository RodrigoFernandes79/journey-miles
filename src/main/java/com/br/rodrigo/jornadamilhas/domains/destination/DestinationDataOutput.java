package com.br.rodrigo.jornadamilhas.domains.destination;

import org.w3c.dom.Text;

import java.math.BigDecimal;

public record DestinationDataOutput(
        String name,
        String photo_1,
        String photo_2,
        String metaDescription,
        String textDescription,
        BigDecimal price) {
    public DestinationDataOutput(Destination destination) {
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
