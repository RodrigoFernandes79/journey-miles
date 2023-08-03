package com.br.rodrigo.jornadamilhas.domains.destination;

import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record DestinationDataInputUpdate(
        @Pattern(regexp = "(.*\\.png|.*\\.jpe?g|.*\\.gif)$", message = "{photo.invalid}")
        String photo,
        BigDecimal price) {

}
