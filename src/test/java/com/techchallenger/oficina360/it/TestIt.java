package com.techchallenger.oficina360.it;

import com.techchallenger.oficina360.constants.Roles;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.repositories.UsuarioRepository;
import com.techchallenger.oficina360.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
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
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;



    @BeforeEach
    void setup() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuario = usuarioRepository.save(
                Usuario.builder()
                        .email(MAIL)
                        .senha(encoder.encode(PASSWORD))
                        .role(Roles.ADMIN)
                        .build()
        );
        token = jwtService.gerarToken(usuario);
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        this.mockMvc.perform(get(PATH)
            .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

    }


}
