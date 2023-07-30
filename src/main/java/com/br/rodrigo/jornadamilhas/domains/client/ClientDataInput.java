package com.br.rodrigo.jornadamilhas.domains.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ClientDataInput(
        @NotBlank(message = "{username.required}")
        String username,
        @NotBlank(message = "{cpf.required}")
        @CPF(message = "{cpf.invalid}")
        String cpf,
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.invalid}")
        String email,
        @NotBlank(message = "{password.required}")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$",
                message = "{password.invalid}")
        String password,
        @NotBlank(message = "{repeat-password.required}")
        String repeatPassword) {
}
