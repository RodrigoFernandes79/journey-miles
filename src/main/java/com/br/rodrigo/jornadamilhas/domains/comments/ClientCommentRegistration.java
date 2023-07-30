package com.br.rodrigo.jornadamilhas.domains.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientCommentRegistration(
        @NotNull(message = "{client.required}")
        Long client_id,
        @NotBlank(message = "{comment.required}")
        String comment) {

}

