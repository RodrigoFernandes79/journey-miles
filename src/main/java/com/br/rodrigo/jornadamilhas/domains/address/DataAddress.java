package com.br.rodrigo.jornadamilhas.domains.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DataAddress(
        @NotBlank(message = "{street.required}")
        String streetOrAvenue,
        @NotBlank(message = "{district.required}")
        String district,
        @NotBlank(message = "{zipCode.required}")
        @Pattern(regexp = "\\d{8}",message = "{zipcode.length}")
        String zipCode,
        String number,
        String apartment,
        @NotBlank(message = "{city.required}")
        String city,
        @NotBlank(message = "{state.required}")
        String state) {
}
