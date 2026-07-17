package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.*;
import static com.techchallenger.oficina360.constants.Roles.*;
import static com.techchallenger.oficina360.mappers.UsuarioMapper.toEntity;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public String autenticar(LoginRequestDTO loginRequestDTO) {
        if(!usuarioRepository.existsByEmail(loginRequestDTO.email())){
            throw new AccessDeniedException(AUTH_SERV_DADOS_DE_LOGIN_INVALIDOS);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.senha()
                );

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        UsuarioEntity usuarioEntity = (UsuarioEntity) authentication.getPrincipal();

        return jwtService.gerarToken(usuarioEntity);
    }
    public void criarUsuario(CriarUsuarioRequestDTO criarUsuarioRequestDTO) {
        if(usuarioRepository.existsByEmail(criarUsuarioRequestDTO.email())) {
            throw new ConflitoException(AUTH_SERV_E_MAIL_JA_CADASTRADO);
        }
        String role = criarUsuarioRequestDTO.role();
        String roleToUpper = role.toUpperCase();
        validaRole(roleToUpper);
        validaSeClienteExiste(criarUsuarioRequestDTO, roleToUpper);

        UsuarioEntity usuarioEntity = toEntity(criarUsuarioRequestDTO);
        usuarioEntity.setRole(roleToUpper);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuarioEntity.setSenha(encoder.encode(criarUsuarioRequestDTO.senha()));

        usuarioRepository.save(usuarioEntity);
    }

    private void validaSeClienteExiste(CriarUsuarioRequestDTO criarUsuarioRequestDTO, String roleToUpper) {
        if(roleToUpper.equals(CLIENTE)){
            clienteRepository.findByDocumento(criarUsuarioRequestDTO.documento())
                    .orElseThrow(()-> new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));
        }
    }

    private void validaRole(String role){
        List<String> rolesPermitidas = List.of(CLIENTE,ADMIN,MECANICO,ATENDENTE);
        if(!rolesPermitidas.contains(role.toUpperCase())){
            throw new RegraDeNegocioException("Role "+ role + "não é valida");
        }
    }
}
