package com.techchallenger.oficina360.dtos.servicos;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.UsuarioRepository;
import com.techchallenger.oficina360.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
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
            throw new AccessDeniedException("Dados de login inválidos");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.email(),
                        loginRequestDTO.senha()
                );

        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String token = jwtService.gerarToken(usuario);
        return token;
    }
    public void criarUsuario(CriarUsuarioRequestDTO criarUsuarioRequestDTO) {
        if(usuarioRepository.existsByEmail(criarUsuarioRequestDTO.email())) {
            throw new ConflitoException("E-mail já cadastrado");
        }
        String role = criarUsuarioRequestDTO.role();
        String roleToUpper = role.toUpperCase();
        validaRole(roleToUpper);
        validaSeClienteExiste(criarUsuarioRequestDTO, roleToUpper);

        Usuario usuario = toEntity(criarUsuarioRequestDTO);
        usuario.setRole(roleToUpper);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario.setSenha(encoder.encode(criarUsuarioRequestDTO.senha()));

        usuarioRepository.save(usuario);
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
