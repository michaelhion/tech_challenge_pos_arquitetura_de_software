package com.techchallenger.oficina360.it;

import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.ServicoFixture.criarValido;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class ServicoIT extends BaseIT{


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private TempoExecucaoServicoRepository tempoExecucaoServicoRepository;

    private static final String ORDEM_SERVICO_BASE_PATH = "/servicos";

    @BeforeEach
    void setup(){
        SecurityContextHolder.clearContext();
        tempoExecucaoServicoRepository.deleteAll();
        servicoRepository.deleteAll();
    }
    @Test
    void deveListarOrdemServico() throws Exception {

        mockMvc.perform(get(ORDEM_SERVICO_BASE_PATH + "/listar")
                .header("Authorization", "Bearer " + tokenAdmin()))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
     void deveCriarServico() throws Exception {

        mockMvc.perform(post(ORDEM_SERVICO_BASE_PATH + "/salvar")
                        .header("Authorization", "Bearer " + tokenAdmin())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(criarValido())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }



    //todo criar fixture
    //todo validar regras de tempo de execução de serviço como se esta mostrando o tempo medio na listagem de acordo com o resultado da querie
    //todo implementar testes para os demais endpoints
    //todo validar se o endpoint de edição realmente esta editando os dados sem criar um novo registro
}