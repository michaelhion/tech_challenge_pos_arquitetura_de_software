package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.gateways.Relogio;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class RelogioSistema implements Relogio {

	private final Clock clock;

	public RelogioSistema(Clock clock) {
		this.clock = clock;
	}

	@Override
	public LocalDateTime agora() {
		return LocalDateTime.now(clock);
	}
}