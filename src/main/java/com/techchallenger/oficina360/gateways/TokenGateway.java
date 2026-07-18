package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Usuario;

public interface TokenGateway {

	String gerarToken(Usuario usuario);

	String validarTokenEObterSubject(String token);

}
