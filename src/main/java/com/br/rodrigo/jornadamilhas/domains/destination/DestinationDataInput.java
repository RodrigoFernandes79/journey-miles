package com.br.rodrigo.jornadamilhas.domains.destination;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record DestinationDataInput(
        @NotBlank(message = "{name.required}")
        String name,
        @Pattern(regexp = "(.*\\.png|.*\\.jpe?g|.*\\.gif)$", message = "{photo.invalid}")
        String photo,
        @NotNull(message = "{price.required}")
        BigDecimal price) {

}
