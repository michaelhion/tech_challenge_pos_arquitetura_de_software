package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
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
        Usuario usuario = obterUsuario(authentication);

        if (!usuarioClienteValido(usuario)) {
            return false;
        }

        return usuario.getDocumento().equals(normalizarDocumento(documento));
    }


    public boolean podeAcessarVeiculo(String placa, Authentication authentication) {
        Usuario usuario = obterUsuario(authentication);

        if (!usuarioClienteValido(usuario)) {
            return false;
        }

        String placaNormalizada = normalizarPlaca(placa);

        return veiculoRepository.findByPlaca(placaNormalizada)
                .map(veiculo -> usuario.getDocumento()
                        .equals(veiculo.getClienteDocumento()))
                .orElse(false);
    }

    public boolean podeAcessarOrdemServico(UUID ordemServicoId, Authentication authentication) {
        Usuario usuario = obterUsuario(authentication);

        if (!usuarioClienteValido(usuario)) {
            return false;
        }

        return ordemServicosRepository.findById(ordemServicoId).map(os -> usuario.getDocumento().equals(os.getDocumentoCliente())).orElse(false);
    }

    private Usuario obterUsuario(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuario)) {
            return null;
        }

        return usuario;
    }

    private boolean usuarioClienteValido(Usuario usuario) {
        return usuario != null && usuario.getDocumento() != null && !usuario.getDocumento().isBlank();
    }
}


