package com.techchallenger.oficina360.it;

import com.jayway.jsonpath.JsonPath;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
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

import java.util.UUID;

import static com.techchallenger.oficina360.enums.OrdemDeServicoStatus.EM_EXECUCAO;
import static com.techchallenger.oficina360.enums.OrdemDeServicoStatus.FINALIZADA;
import static com.techchallenger.oficina360.it.fixtures.ordemservico.OrdemServicoFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(value = "test")
class OrdemServicoIT {

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
    private OrdemServicosRepository ordemServicosRepository;

    private String tokenAdmin;
    private String tokenCliente;

    private static final String ORDEM_SERVICO_BASE_PATH = "/ordem-servico";
    private static final String ORDEM_SERVICO_CLIENTE_BASE_PATH = "/ordem-servico/clientes";


    @BeforeEach
    void setup() {
        tokenAdmin = jwtService.gerarToken(gerarUsuarioAdmin());
        tokenCliente = jwtService.gerarToken(gerarUsuarioCliente());
    }

    @Test
    void deveListarOrdemServico() throws Exception {

        mockMvc.perform(get(ORDEM_SERVICO_BASE_PATH + "/listar")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
     void deveCriarOrdemServico() throws Exception {

        mockMvc.perform(post(ORDEM_SERVICO_BASE_PATH + "/salvar")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(ordemServicoValida())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void devePassarPorTodosStatus() throws Exception {

        String response = mockMvc.perform(post(ORDEM_SERVICO_BASE_PATH + "/salvar")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(ordemServicoValida())))
                .andExpect(status().isCreated())
                .andExpect( jsonPath("$.ordemDeServicoStatus").value("RECEBIDA"))
                .andReturn().getResponse().getContentAsString();

        String id = JsonPath.read(response, "$.id");


        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH+ "/%s/diagnostico".formatted(id))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(diagnosticoDTOValido())))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.ordemDeServicoStatus").value("AGUARDANDO_APROVACAO"))
                .andDo(print());

        String tokenCliente = jwtService.gerarToken(gerarUsuarioCliente2());

        mockMvc.perform(patch((ORDEM_SERVICO_CLIENTE_BASE_PATH + "/aprovacao/%s").formatted(id))
                        .header("Authorization", "Bearer " + tokenCliente)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(aprovacaoOrdemServicoDTOValido())))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.ordemDeServicoStatus").value("ORCAMENTO_APROVADO"))
                .andDo(print());

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/iniciar/%s".formatted(id))
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isAccepted())
                .andDo(print());

        OrdemServicoEntity os = ordemServicosRepository.findById(UUID.fromString(id)).orElseThrow();
        assertEquals(EM_EXECUCAO, os.getOrdemDeServicoStatus());

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/finalizar/%s".formatted(id))
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isAccepted())
                .andDo(print());

        os = ordemServicosRepository.findById(UUID.fromString(id)).orElseThrow();
        assertEquals(FINALIZADA, os.getOrdemDeServicoStatus());
    }



    @Test
    void naoDeveCriarOrdemServicoParaVeiculoDeOutroCliente() throws Exception {

         mockMvc.perform(post(ORDEM_SERVICO_BASE_PATH + "/salvar")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(ordemServicoPlacaDeOutroCliente())))
                .andExpect(status().isUnprocessableContent())
                .andDo(print());
    }

    @Test
    void deveDiagnosticar() throws Exception {

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH+ "/%s/diagnostico".formatted(OS_COM_STATUS_RECEBIDA))
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(diagnosticoDTOValido())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deveAprovarOrcamento() throws Exception {

        mockMvc.perform(patch((ORDEM_SERVICO_CLIENTE_BASE_PATH + "/aprovacao/%s").formatted(OS_COM_STATUS_AGUARDANDO_APROVACAO))
                .header("Authorization", "Bearer " + tokenCliente)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(aprovacaoOrdemServicoDTOValido())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deveIniciarExecucao() throws Exception {

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/iniciar/%s".formatted(OS_COM_STATUS_APROVADA))
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    void naoDeveIniciarExecucaoDeOrdemDeServicoReprovada() throws Exception {

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/iniciar/%s".formatted(OS_COM_STATUS_REPROVADA))
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
   void deveFinalizarExecucao() throws Exception {

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/finalizar/%s".formatted(OS_COM_STATUS_EM_EXECUCAO))
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    void clienteNaodeveFinalizarExecucao() throws Exception {
        UsuarioEntity usuarioCliente = gerarUsuarioCliente1();
        String token = jwtService.gerarToken(usuarioCliente);

        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/finalizar/%s".formatted(OS_COM_STATUS_EM_EXECUCAO))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }



    @Test
    void validarStatusFinal() throws Exception {

        mockMvc.perform(get(ORDEM_SERVICO_BASE_PATH + "/listar/%s".formatted(OS_COM_STATUS_FINALIZADA))
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.ordemDeServicoStatus").value("FINALIZADA"));
    }

    @Test
    void naoDeveAprovarOrcamentoDeOutroCliente() throws Exception {

        UsuarioEntity usuarioCliente = gerarUsuarioCliente1();
        String token = jwtService.gerarToken(usuarioCliente);


        mockMvc.perform(patch((ORDEM_SERVICO_CLIENTE_BASE_PATH + "/aprovacao/%s").formatted(OS_COM_STATUS_AGUARDANDO_APROVACAO))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(aprovacaoOrdemServicoDTOValido())))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void naoDeveFinalizarOrdemNaoIniciada() throws Exception {
        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/finalizar/%s".formatted(OS_COM_STATUS_RECEBIDA))
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void clienteNaoDeveIniciarExecucao() throws Exception {
        UsuarioEntity usuarioCliente = gerarUsuarioCliente1();
        String token = jwtService.gerarToken(usuarioCliente);
        mockMvc.perform(patch(ORDEM_SERVICO_BASE_PATH + "/execucao/iniciar/%s".formatted(OS_COM_STATUS_APROVADA))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    //todo validar comportamento de adição de serviços na os, validando valores e se todos serviços requeridos foram adicionados
    //todo validar o que acontece quando tenta inserir um serviço que não existe ou um que existe e outro que não existe, deve retornar um bad request e interromper o fluxo
    //todo validar o comportamento da adição de estoque na os com os mesmos testes do serviço
    //todo validar edição de ordem de serviço, incluindo validar se realmente editou ou se criou novo registro no banco com id diferente

}