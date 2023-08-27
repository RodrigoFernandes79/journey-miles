package com.br.rodrigo.jornadamilhas.services.security;

import com.br.rodrigo.jornadamilhas.domains.user.User;

public record DataTokenJWT(String token, String name, String email) {

}
