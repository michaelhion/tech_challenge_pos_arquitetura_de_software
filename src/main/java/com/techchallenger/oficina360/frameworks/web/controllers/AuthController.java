package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.AuthApi;
import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import com.techchallenger.oficina360.usecases.auth.AutenticarUsuarioUseCase;
import com.techchallenger.oficina360.usecases.auth.CriarUsuarioUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.techchallenger.oficina360.constants.Roles.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        String token = autenticarUsuarioUseCase.executar(loginRequestDTO);

        return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer"));
    }



    @PostMapping("/criar-usuario")
    @PreAuthorize("hasRole('" + ADMIN + "')")
    public ResponseEntity<String> criarUsuario(@Valid @RequestBody CriarUsuarioRequestDTO criarUsuarioRequestDTO) {
        criarUsuarioUseCase.executar(criarUsuarioRequestDTO);
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}