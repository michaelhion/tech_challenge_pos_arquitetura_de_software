# Documento de Requisitos – Oficina360

## 1. Introdução

### 1.1 Objetivo

O sistema **Oficina360** é uma API REST para gestão de oficinas mecânicas, permitindo o gerenciamento de clientes, veículos, serviços, estoque, ordens de serviço e controle operacional da execução dos reparos.

O sistema também disponibiliza uma área de acesso para clientes acompanharem suas próprias ordens de serviço e aprovarem orçamentos.

---

## 2. Escopo

O sistema deve possibilitar:

- Cadastro e manutenção de clientes;
- Cadastro e manutenção de veículos;
- Cadastro e manutenção de serviços;
- Controle de estoque de peças e insumos;
- Abertura e acompanhamento de ordens de serviço;
- Realização de diagnósticos;
- Aprovação de orçamentos;
- Controle da execução dos serviços;
- Controle de entrega dos veículos;
- Apuração de tempo médio de execução dos serviços;
- Autenticação e autorização via JWT;
- Controle de acesso baseado em perfis.

---

# 3. Requisitos Funcionais

## RF001 – Autenticação de Usuários

O sistema deve permitir que usuários autenticados realizem login utilizando e-mail e senha.

### Entradas

- E-mail
- Senha

### Saídas

- Token JWT
- Perfil do usuário

## RF002 – Controle de Acesso por Perfil

O sistema deve controlar o acesso aos endpoints de acordo com o perfil do usuário.

Perfis suportados:

- ADMIN
- ATENDENTE
- MECANICO
- ESTOQUISTA
- CLIENTE

## RF003 – Cadastro de Clientes

O sistema deve permitir o cadastro de clientes contendo:

- Nome
- Documento (CPF/CNPJ)
- E-mail
- Telefone

## RF004 – Consulta de Clientes

O sistema deve permitir consultar clientes cadastrados.

Restrições:

- Clientes podem visualizar apenas seus próprios dados.

## RF005 – Atualização de Clientes

O sistema deve permitir atualizar os dados cadastrais de clientes.

## RF006 – Exclusão de Clientes

O sistema deve permitir excluir clientes cadastrados.

## RF007 – Cadastro de Veículos

O sistema deve permitir cadastrar veículos vinculados a um cliente.

Campos:

- Placa
- Marca
- Modelo
- Ano
- Documento do cliente

## RF008 – Consulta de Veículos

O sistema deve permitir consultar veículos.

Restrições:

- Clientes podem consultar apenas veículos de sua propriedade.

## RF009 – Atualização de Veículos

O sistema deve permitir editar dados de veículos cadastrados.

## RF010 – Exclusão de Veículos

O sistema deve permitir remover veículos cadastrados.

## RF011 – Cadastro de Serviços

O sistema deve permitir cadastrar serviços oferecidos pela oficina.

Campos:

- Código
- Descrição
- Valor

## RF012 – Consulta de Serviços

O sistema deve permitir listar e consultar serviços cadastrados.

O sistema deve apresentar:

- Código
- Descrição
- Valor
- Tempo médio de execução

## RF013 – Atualização de Serviços

O sistema deve permitir atualizar serviços cadastrados.

## RF014 – Exclusão de Serviços

O sistema deve permitir excluir serviços cadastrados.

## RF015 – Cadastro de Estoque

O sistema deve permitir cadastrar peças e insumos.

Campos:

- Código
- Nome
- Valor
- Quantidade

## RF016 – Consulta de Estoque

O sistema deve permitir consultar itens de estoque.

## RF017 – Atualização de Estoque

O sistema deve permitir editar itens de estoque.

## RF018 – Controle de Reservas de Estoque

O sistema deve controlar a reserva de peças e insumos utilizados em diagnósticos.

O sistema deve:

- Reservar itens durante o diagnóstico;
- Impedir uso de quantidade superior à disponível.

## RF019 – Abertura de Ordem de Serviço

O sistema deve permitir abrir uma ordem de serviço para um veículo.

Campos:

- Cliente
- Veículo
- Problema informado

Status inicial:

```text
RECEBIDA
```

## RF020 – Consulta de Ordem de Serviço

O sistema deve permitir consultar ordens de serviço.

Restrições:

- Clientes podem acessar apenas suas próprias ordens de serviço.

## RF021 – Diagnóstico da Ordem de Serviço

O sistema deve permitir registrar diagnósticos.

O diagnóstico deverá conter:

- Serviços necessários
- Peças e insumos necessários

## RF022 – Cálculo Financeiro da Ordem de Serviço

O sistema deve calcular automaticamente:

- Valor dos serviços
- Valor das peças e insumos
- Valor total da ordem

## RF023 – Aprovação de Orçamento

O sistema deve permitir que o cliente:

- Aprove o orçamento; ou
- Reprove o orçamento

Podendo incluir observações.

## RF024 – Início da Execução

O sistema deve permitir registrar o início da execução dos serviços.

## RF025 – Finalização da Execução

O sistema deve permitir registrar a finalização da execução dos serviços.

## RF026 – Entrega do Veículo

O sistema deve permitir registrar a entrega do veículo.

## RF027 – Histórico de Tempo de Execução

O sistema deve armazenar o tempo de execução dos serviços realizados.

## RF028 – Cálculo do Tempo Médio dos Serviços

O sistema deve calcular automaticamente a média dos tempos registrados para cada serviço.

## RF029 – Controle de Status da Ordem de Serviço

Fluxo:

```text
RECEBIDA
↓
AGUARDANDO_APROVACAO
↓
ORCAMENTO_APROVADO
↓
EM_EXECUCAO
↓
FINALIZADA
↓
ENTREGUE
```

## RF030 – Registro de Observações do Cliente

O sistema deve permitir que clientes adicionem observações ao aprovar ou reprovar orçamentos.

# 4. Requisitos Não Funcionais

## RNF001 – Arquitetura

Arquitetura em camadas: Controller, Service, Repository, Entity, DTO e Mapper.

## RNF002 – API REST

Comunicação via HTTP/JSON.

## RNF003 – Persistência de Dados

Utilização de banco de dados relacional.

## RNF004 – Controle de Migrações

Versionamento do banco utilizando Flyway.

## RNF005 – Segurança de Senhas

Armazenamento de senhas utilizando BCrypt.

## RNF006 – Autenticação

Autenticação baseada em JWT.

## RNF007 – Autorização

Autorização baseada em perfis.

## RNF008 – Controle de Propriedade do Recurso

Clientes somente poderão acessar recursos que lhes pertençam.

## RNF009 – Proteção de Dados Pessoais

Adoção dos princípios da LGPD.

## RNF010 – Documentação da API

Documentação Swagger/OpenAPI.

## RNF011 – Logs

Registro de requisições, respostas e tempo de processamento.

## RNF012 – Containerização

Execução via Docker.

## RNF013 – Segurança do Container

Execução com usuário não-root.

## RNF014 – Tratamento de Exceções

Tratamento centralizado de exceções.

## RNF015 – Integridade dos Dados

Garantia de unicidade e integridade referencial.

## RNF016 – Transações

Operações críticas executadas de forma transacional.

## RNF017 – Manutenibilidade

Aplicação de SRP e separação de responsabilidades.

## RNF018 – Testabilidade

Suporte a testes unitários, integração e segurança.

## RNF019 – Disponibilidade da Documentação

Documentação disponível em ambiente de desenvolvimento.

## RNF020 – Rastreabilidade

Histórico de execução dos serviços para métricas operacionais.

# 5. Regras de Negócio

- RN001: Não pode existir mais de um veículo com a mesma placa.
- RN002: Uma OS só pode ser aberta para um veículo existente.
- RN003: Uma OS só pode ser aberta para um cliente existente.
- RN004: Não pode ser reservado estoque acima da quantidade disponível.
- RN005: Somente clientes proprietários da ordem podem aprová-la.
- RN006: Somente MECANICO ou ADMIN podem iniciar/finalizar execuções.
- RN007: Somente ESTOQUISTA ou ADMIN podem administrar estoque.
- RN008: O tempo médio deve ser calculado com base nas execuções registradas.
- RN009: A execução só pode iniciar após aprovação do orçamento.
- RN010: A entrega só pode ocorrer após finalização da execução.

# 6. Critérios de Aceitação

O sistema será considerado concluído quando:

- Todos os CRUDs estiverem operacionais;
- O fluxo completo da OS estiver funcional;
- A autenticação JWT estiver operacional;
- O controle de perfis estiver implementado;
- Clientes acessarem apenas seus próprios dados;
- O cálculo financeiro da OS estiver correto;
- O tempo médio dos serviços estiver sendo calculado automaticamente;
- A documentação Swagger estiver disponível;
- A aplicação puder ser executada via Docker;
- Os testes automatizados estiverem executando com sucesso.
