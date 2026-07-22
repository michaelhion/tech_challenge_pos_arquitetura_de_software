package com.techchallenger.oficina360.it;

import com.techchallenger.oficina360.constants.Roles;
import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import com.techchallenger.oficina360.gateways.TokenGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestIt {

    private static final String PATH = "/clientes/listar";
    private static final String MAIL = "admin1@oficina360.com";
    private static final String PASSWORD = "123456";
    private Usuario usuario;
    private String token;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenGateway tokenGateway;

    @Autowired
    private UsuarioGateway usuarioRepository;



    @BeforeEach
    void setup() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario = usuarioRepository.save(
                new Usuario(
                        null,
                        MAIL,
                        encoder.encode(PASSWORD),
                        Roles.ADMIN,
                        "12345678910"
                )
        );
        token = tokenGateway.gerarToken(usuario);
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        this.mockMvc.perform(get(PATH)
            .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

    }


}
