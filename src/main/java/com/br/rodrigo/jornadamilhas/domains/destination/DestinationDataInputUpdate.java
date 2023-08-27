package com.br.rodrigo.jornadamilhas.domains.destination;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.w3c.dom.Text;

import java.math.BigDecimal;

public record DestinationDataInputUpdate(
        @NotBlank(message = "{photo1.required}")
        @Pattern(regexp = "(.*\\.png|.*\\.jpe?g|.*\\.gif)$", message = "{photo.invalid}")
        String photo_1,
        @NotBlank(message = "{photo2.required}")
        @Pattern(regexp = "(.*\\.png|.*\\.jpe?g|.*\\.gif)$", message = "{photo.invalid}")
        String photo_2,
        @NotBlank(message = "{meta.required}")
        @Length(max = 160, message = "{meta.length}")
        String metaDescription,
        String textDescription,
        BigDecimal price
) {

}
