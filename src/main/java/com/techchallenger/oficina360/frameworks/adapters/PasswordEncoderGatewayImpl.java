package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.gateways.PasswordEncoderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderGatewayImpl implements PasswordEncoderGateway {

	private final PasswordEncoder passwordEncoder;

	@Override
	public String criptografar(String openValue) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(openValue);
	}
}
