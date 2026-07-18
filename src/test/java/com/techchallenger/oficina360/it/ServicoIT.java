package com.techchallenger.oficina360.it;

import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import com.techchallenger.oficina360.security.JwtService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.techchallenger.oficina360.it.fixtures.ordemservico.ServicoFixture.criarValido;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class ServicoIT extends BaseIT{

    private static final String OS_COM_STATUS_RECEBIDA = "76dba7d9-2ded-426f-aae8-fd8f8506a7cc";
    private static final String OS_COM_STATUS_AGUARDANDO_APROVACAO = "cc702d41-3acf-464f-ad33-f9ec8c01f57d";
    private static final String OS_COM_STATUS_APROVADA = "2b3a19fd-d3c0-4d9f-9738-6fec31269023";
    private static final String OS_COM_STATUS_REPROVADA = "0b49c552-8ba0-4c0b-bcec-42db82526af9";
    private static final String OS_COM_STATUS_EM_EXECUCAO = "4baecc4b-57d2-419b-b080-ae6615a44052";
    private static final String OS_COM_STATUS_FINALIZADA = "8c7d79a6-370d-4007-a772-a8e22420fbfb";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private TempoExecucaoServicoRepository tempoExecucaoServicoRepository;

    private static final String ORDEM_SERVICO_BASE_PATH = "/servicos";

    @BeforeEach
    void setup(){
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