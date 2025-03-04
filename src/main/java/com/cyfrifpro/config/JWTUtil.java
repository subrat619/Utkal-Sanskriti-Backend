package com.cyfrifpro.config;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {

	@Value("${jwt_secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationTimeMillis;

	public String generateToken(Long userId, String email, String role) {
		return JWT.create().withSubject("User Details").withClaim("userId", userId).withClaim("email", email)
				.withClaim("role", role).withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMillis)).withIssuer("CyfrifPro Tech")
				.sign(Algorithm.HMAC256(secret));
	}

	public Map<String, Object> validateTokenAndRetrieveClaims(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withSubject("User Details")
				.withIssuer("CyfrifPro Tech").build();
		var jwt = verifier.verify(token);

		return Map.of("userId", jwt.getClaim("userId").asLong(), "email", jwt.getClaim("email").asString(), "role",
				jwt.getClaim("role").asString());
	}

}
