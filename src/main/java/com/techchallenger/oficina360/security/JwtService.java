package com.techchallenger.oficina360.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.techchallenger.oficina360.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${api.security.jwt.secret}")
    private String secret;

    @Value("${api.security.jwt.issuer:oficina360-api}")
    private String issuer;

    @Value("${api.security.jwt.expiration-hours:2}")
    private Long expirationHours;

    public String gerarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(usuario.getEmail())
                .withClaim("role", usuario.getRole())
                .withExpiresAt(dataExpiracao())
                .sign(algorithm);
    }

    public String validarTokenEObterSubject(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now()
                .plusHours(expirationHours)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}