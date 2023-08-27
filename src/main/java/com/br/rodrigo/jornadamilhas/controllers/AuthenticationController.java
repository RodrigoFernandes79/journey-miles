package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.user.User;
import com.br.rodrigo.jornadamilhas.domains.user.UserDataAuthentication;
import com.br.rodrigo.jornadamilhas.services.security.DataTokenJWT;
import com.br.rodrigo.jornadamilhas.services.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    public ResponseEntity logIn(@RequestBody @Valid UserDataAuthentication userData) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(userData.login(), userData.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
        var name = ((User) authentication.getPrincipal()).getName();
        var email = ((User) authentication.getPrincipal()).getEmail();
        return ResponseEntity.ok(new DataTokenJWT(tokenJWT, name, email));
    }

}
