# Oficina360 API

API REST desenvolvida para o **Tech Challenge - Fase 1**, com o objetivo de criar a primeira versão MVP de um sistema integrado para gestão de oficina mecânica.

O sistema busca organizar o fluxo de atendimento, cadastro de clientes, veículos, serviços, estoque de peças/insumos e futuramente ordens de serviço, permitindo maior controle operacional e rastreabilidade dos processos internos.

---

## 📌 Objetivo do projeto

O **Oficina360** é um backend monolítico para apoiar uma oficina mecânica na gestão dos seus principais processos administrativos e operacionais.

O projeto foi desenvolvido com foco em:

- Cadastro e gestão de clientes;
- Cadastro e gestão de veículos;
- Cadastro e gestão de serviços;
- Cadastro e controle de estoque de peças e insumos;
- Reserva de itens de estoque;
- Base para criação e acompanhamento de ordens de serviço;
- Documentação da API via Swagger/OpenAPI;
- Uso de boas práticas de camadas, validação e tratamento de exceções;
- Migração de banco de dados com Flyway;
- Testes unitários.

---

## 🧩 Funcionalidades implementadas

### Clientes

- Cadastrar cliente;
- Listar clientes;
- Buscar cliente por documento CPF/CNPJ;
- Editar cliente;
- Excluir cliente por documento.

### Veículos

- Cadastrar veículo;
- Listar veículos;
- Buscar veículo por placa;
- Editar veículo por placa;
- Excluir veículo por placa;
- Associação fraca com cliente por meio do documento do cliente.

### Serviços

- Cadastrar serviço;
- Listar serviços;
- Buscar serviço por ID;
- Editar serviço;
- Excluir serviço.

### Estoque

- Cadastrar item de estoque;
- Listar itens de estoque;
- Buscar item de estoque por ID;
- Editar item de estoque;
- Excluir item de estoque;
- Controlar quantidade total;
- Controlar quantidade reservada;
- Calcular quantidade disponível;
- Reservar quantidade de item em estoque.

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura monolítica em camadas, adequada para o MVP proposto.

Principais camadas:

```text
Controller -> Service -> Repository -> Entity
```

Também foram utilizados:

- DTOs para entrada e saída de dados;
- Mappers para conversão entre DTO e Entity;
- Exceptions personalizadas;
- Constants para documentação Swagger;
- Configuração centralizada do OpenAPI.

---

## 📁 Estrutura do projeto

```text
├── .mvn
│   └── wrapper
├── docker
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── techchallenger
│   │   │           └── oficina360
│   │   │               ├── config
│   │   │               ├── controllers
│   │   │               ├── docs
│   │   │               ├── dtos
│   │   │               ├── entities
│   │   │               ├── enums
│   │   │               ├── exceptions
│   │   │               ├── mappers
│   │   │               ├── repositories
│   │   │               └── services
|   |   |               └── security
│   │   └── resources
│   │       ├── application.yml
│   │       └── db
│   │           └── migration
│   └── test
│       └── java
│           └── com
│               └── techchallenger
│                   └── oficina360
│                       ├── controllers
│                       └── services
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- Flyway
- PostgreSQL
- Docker
- Docker Compose
- Swagger/OpenAPI com Springdoc
- JUnit 5
- Mockito
- Lombok
- Maven

---

## 🗄️ Banco de dados

O projeto utiliza banco relacional, com versionamento de schema via **Flyway**.

As migrations ficam em:

```text
src/main/resources/db/migration
```

Exemplo de migrations:

```text
V1__create_tables.sql
V2__insert_massa_inicial_clientes_veiculos.sql
```

---

## 📚 Documentação da API

A documentação da API é gerada automaticamente com Swagger/OpenAPI.

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

Ou, dependendo da configuração:

```text
http://localhost:8080/swagger-ui.html
```

A documentação contém:

- Descrição geral do sistema;
- Endpoints separados por tags;
- Contratos dos DTOs;
- Exemplos de payload;
- Possíveis códigos HTTP;
- Configuração para autenticação JWT via botão `Authorize`.

---

## 🔐 Segurança

O projeto foi preparado para documentação de autenticação JWT no Swagger.

No Swagger, o esquema configurado é:

```text
bearerAuth
```

Formato esperado:

```text
Authorization: Bearer {token}
```

> A implementação completa da autenticação JWT deve ser integrada aos endpoints administrativos conforme evolução do projeto.

---

## 🚀 Como executar localmente

### Pré-requisitos

Antes de iniciar, tenha instalado:

- Java 21;
- Maven;
- Docker;
- Docker Compose.

### Executando com Maven

Clone o projeto:

```bash
git clone <url-do-repositorio>
```

Acesse a pasta:

```bash
cd oficina360
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

### Executando com Docker Compose

Suba os containers:

```bash
docker-compose up --build
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

O Swagger ficará disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

Para parar os containers:

```bash
docker-compose down
```

---

## 🧪 Executando os testes

Para executar os testes unitários:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

Os testes cobrem principalmente:

- Services;
- Controllers;
- Fluxos de cadastro;
- Busca;
- Edição;
- Exclusão;
- Tratamento de registros inexistentes;
- Conflitos de regra de negócio.

---

## 📦 Principais endpoints

### Clientes

```http
GET    /clientes/listar
GET    /clientes/listar/{documento}
POST   /clientes/salvar
PUT    /clientes/editar/{id}
DELETE /clientes/deletar/{documento}
```

### Veículos

```http
GET    /veiculos/listar
GET    /veiculos/listar/{placa}
POST   /veiculos/salvar
PUT    /veiculos/editar/{placa}
DELETE /veiculos/deletar/{placa}
```

### Serviços

```http
GET    /servicos/listar
GET    /servicos/listar/{id}
POST   /servicos/salvar
PUT    /servicos/editar/{id}
DELETE /servicos/deletar/{id}
```

### Estoque

```http
GET    /estoque/listar
GET    /estoque/listar/{id}
POST   /estoque/salvar
PUT    /estoque/editar/{id}
DELETE /estoque/deletar/{id}
PATCH  /estoque/reservar/{id}
```

---

## Autenticação

Para acessar endpoints administrativos, primeiro realize login:

POST /auth/login

```json
{
  "email": "admin@oficina360.com",
  "senha": "123456"
}
```
---

## 📥 Exemplos de payload

### Cadastrar cliente

```json
{
  "documento": "12345678901",
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "telefone": "11999999999"
}
```

### Cadastrar veículo

```json
{
  "placa": "ABC1D23",
  "marca": "Volkswagen",
  "modelo": "Gol",
  "ano": 2020,
  "clienteDocumento": "12345678901"
}
```

### Cadastrar serviço

```json
{
  "descricao": "Troca de óleo",
  "valor": 150.00
}
```

### Cadastrar item de estoque

```json
{
  "nome": "Filtro de óleo",
  "valor": 45.90,
  "quantidade": 20,
  "reservados": 0,
  "disponiveis": 20
}
```

### Reservar item de estoque

```json
{
  "quantidade": 3
}
```

---

## ⚠️ Tratamento de erros

O projeto utiliza exceptions personalizadas para representar erros de negócio e erros de aplicação.

Principais exemplos:

- `RecursoNaoEncontradoException`
- `ConflitoException`
- `RegraDeNegocioException`

Exemplo de resposta de erro:

```json
{
  "status": 404,
  "erro": "Recurso não encontrado",
  "mensagem": "Item de estoque não encontrado",
  "timestamp": "2026-06-07T19:03:00"
}
```

---

## 🧾 Regras de negócio relevantes

### Documento do cliente

O cliente é identificado por CPF ou CNPJ, informado somente com números.

Exemplos:

```text
CPF: 12345678901
CNPJ: 11222333000181
```

### Placa do veículo

O veículo pode ser identificado pela placa no padrão antigo ou Mercosul.

Exemplos:

```text
ABC1234
ABC1D23
```

### Estoque disponível

A quantidade disponível é calculada por:

```text
disponiveis = quantidade - reservados
```

Esse valor não deve ser persistido diretamente no banco, pois é derivado dos campos `quantidade` e `reservados`.

### Reserva de estoque

Ao reservar um item, o sistema aumenta a quantidade de `reservados`, reduzindo a quantidade disponível para novas ordens de serviço.

Exemplo:

```text
quantidade = 20
reservados = 5
disponiveis = 15
```

Se o usuário tentar reservar uma quantidade maior que a disponível, o sistema deve retornar erro de estoque indisponível.

---

## 🧱 Decisões técnicas

### Associação fraca entre veículo e cliente

Para simplificar o MVP, a associação entre veículo e cliente é feita por meio do campo:

```text
clienteDocumento
```

Essa abordagem evita complexidade inicial com relacionamentos JPA como `@ManyToOne` e `@OneToMany`, mantendo o vínculo por meio de um identificador de negócio conhecido pelo usuário.

### Uso de DTOs

Os controllers utilizam DTOs em vez de expor diretamente as entidades JPA.

Benefícios:

- Menor acoplamento;
- Maior segurança;
- Melhor documentação no Swagger;
- Facilidade de validação;
- Melhor controle do contrato da API.

### Uso de Mappers

As conversões entre DTO e Entity são feitas em classes Mapper, evitando que controllers ou entidades assumam responsabilidades indevidas.

### Configuração centralizada do Swagger

O projeto utiliza uma configuração central de OpenAPI para documentar:

- Nome e descrição da API;
- Versão;
- Contato;
- Autenticação JWT;
- Tags dos módulos;
- Servidores disponíveis.

---

## 🧪 Cobertura de testes

O projeto possui testes unitários para:

- `ClienteService`
- `ClientesController`
- `VeiculoService`
- `VeiculosController`
- `ServicoService`
- `ServicosController`
- Demais serviços e controllers conforme evolução do MVP.

Objetivo de cobertura:

```text
mínimo de 80% nos domínios críticos
```

---

## 🐳 Docker

O projeto deve conter:

```text
Dockerfile
docker-compose.yml
```

Exemplo de execução:

```bash
docker-compose up --build
```

Para parar os containers:

```bash
docker-compose down
```

---

## 📌 Status do projeto

Projeto em desenvolvimento para entrega do MVP da Fase 1.

Funcionalidades concluídas ou em andamento:

- [x] CRUD de clientes
- [x] CRUD de veículos
- [x] CRUD de serviços
- [x] CRUD de estoque
- [x] Reserva de estoque
- [x] Swagger/OpenAPI
- [x] Flyway
- [x] Testes unitários
- [ ] Autenticação JWT completa
- [ ] Gestão completa de ordens de serviço
- [ ] Acompanhamento de status da OS
- [ ] Relatório de vulnerabilidades

---

## 👥 Grupo

Preencher antes da entrega:

```text
Nome do grupo:
Participantes:
Discord usernames:
Link da documentação DDD:
Link do repositório:
```

---

## 📄 Entrega

Este projeto faz parte do Tech Challenge da Fase 1.

Entregáveis esperados:

- Código-fonte em repositório privado;
- README com instruções de execução;
- Dockerfile;
- docker-compose.yml;
- APIs documentadas via Swagger;
- Testes automatizados;
- Documentação DDD;
- Relatório de vulnerabilidades;
- Documento final em PDF com links e informações do grupo.

---

## 👨‍💻 Autor

Projeto desenvolvido para fins acadêmicos no contexto do Tech Challenge.
