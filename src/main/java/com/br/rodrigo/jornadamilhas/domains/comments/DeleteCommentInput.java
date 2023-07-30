package com.br.rodrigo.jornadamilhas.domains.comments;

import jakarta.validation.constraints.NotBlank;

public record DeleteCommentInput(
        @NotBlank(message = "{client.required}")
        Long client_id) {
}
