package com.br.rodrigo.jornadamilhas.domains.client;

import com.br.rodrigo.jornadamilhas.domains.address.DataAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ClientDataInputUpdate(
        @NotBlank(message = "{phone-number.required}")
        @Length(min = 10, max = 15, message = "{phone-number.length}")
        String phoneNumber,
        @Pattern(regexp = "(.*\\.png|.*\\.jpe?g|.*\\.gif)$", message = "{photo.invalid}")
        String photo,
        @NotNull
        @Valid
        DataAddress dataAddress) {

}
