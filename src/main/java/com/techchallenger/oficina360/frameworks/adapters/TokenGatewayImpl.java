package com.techchallenger.oficina360.frameworks.adapters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.gateways.TokenGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.AMERICA_SAO_PAULO;

@Component
@RequiredArgsConstructor
public class TokenGatewayImpl implements TokenGateway {

	@Value("${api.security.jwt.secret}")
	private String secret;

	@Value("${api.security.jwt.issuer:oficina360-api}")
	private String issuer;

	@Value("${api.security.jwt.expiration-hours:2}")
	private Long expirationHours;

	@Override
	public String gerarToken(Usuario usuario) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create()
				.withIssuer(issuer)
				.withSubject(usuario.getEmail())
				.withClaim("role", usuario.getRole())
				.withExpiresAt(dataExpiracao())
				.sign(algorithm);
	}

	@Override
	public String validarTokenEObterSubject(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.require(algorithm)
				.withIssuer(issuer)
				.build()
				.verify(token)
				.getSubject();
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO))
				.plusHours(expirationHours)
				.toInstant(ZoneOffset.of("-03:00"));
	}
}
