package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

@Component("autorizacaoCliente")
@RequiredArgsConstructor
public class AutorizacaoClienteService {


    private final VeiculoRepository veiculoRepository;
    private final OrdemServicosRepository ordemServicosRepository;

    public boolean podeAcessarClientePorDocumento(String documento, Authentication authentication) {
        UsuarioEntity usuarioEntity = obterUsuario(authentication);

        if (!usuarioClienteValido(usuarioEntity)) {
            return false;
        }

        return usuarioEntity.getDocumento().equals(normalizarDocumento(documento));
    }


    public boolean podeAcessarVeiculo(String placa, Authentication authentication) {
        UsuarioEntity usuarioEntity = obterUsuario(authentication);

        if (!usuarioClienteValido(usuarioEntity)) {
            return false;
        }

        String placaNormalizada = normalizarPlaca(placa);

        return veiculoRepository.findByPlaca(placaNormalizada)
                .map(veiculo -> usuarioEntity.getDocumento()
                        .equals(veiculo.getClienteDocumento()))
                .orElse(false);
    }

    public boolean podeAcessarOrdemServico(UUID ordemServicoId, Authentication authentication) {
        UsuarioEntity usuarioEntity = obterUsuario(authentication);

        if (!usuarioClienteValido(usuarioEntity)) {
            return false;
        }

        return ordemServicosRepository.findById(ordemServicoId).map(os -> usuarioEntity.getDocumento().equals(os.getDocumentoCliente())).orElse(false);
    }

    private UsuarioEntity obterUsuario(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioEntity usuarioEntity)) {
            return null;
        }

        return usuarioEntity;
    }

    private boolean usuarioClienteValido(UsuarioEntity usuarioEntity) {
        return usuarioEntity != null && usuarioEntity.getDocumento() != null && !usuarioEntity.getDocumento().isBlank();
    }
}


