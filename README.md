# Oficina360 API

## 📖 Sobre o Projeto

O **Oficina360** é uma API REST desenvolvida como parte do Tech Challenge da Pós-Graduação em Arquitetura de Software.

O objetivo da aplicação é fornecer uma solução completa para o gerenciamento de oficinas mecânicas, contemplando desde o cadastro de clientes e veículos até o gerenciamento completo de ordens de serviço, controle de estoque, autenticação de usuários, autorização de acesso e indicadores operacionais.

O projeto foi desenvolvido seguindo princípios de:

- Domain-Driven Design (DDD);
- Arquitetura em Camadas;
- Boas Práticas de Engenharia de Software;
- Segurança;
- Observabilidade;
- Testes Automatizados;
- Integração Contínua;
- Documentação Arquitetural baseada no Modelo C4.

---

# 📚 Documentação

Toda a documentação arquitetural encontra-se na pasta:

```text
docs/
```

## Documentação Principal

📄 [Arquitetura da Solução](docs/ARQUITETURA.md)

## Documentos de Apoio

📄 [Documento de Requisitos](docs/requisitos/Documento_Requisitos_Oficina360.md)

📄 [Linguagem Ubíqua](docs/requisitos/LINGUAGEM-UBIQUA.md)

---

# 🧠 Domain-Driven Design (DDD)

A modelagem do domínio foi construída utilizando conceitos de Domain-Driven Design (DDD).

## Artefatos Produzidos

- Domain Storytelling;
- Linguagem Ubíqua;
- Classificação dos Subdomínios;
- Context Map;
- Modelo C4.

## Core Domain

- Ordem de Serviço

## Supporting Domains

- Cliente Context
- Veículo Context
- Serviço Context
- Estoque Context

Documentação completa:

📄 [Arquitetura da Solução](docs/ARQUITETURA.md)

---

# 🎯 Objetivos

A plataforma busca oferecer suporte aos principais processos de uma oficina mecânica:

- Cadastro e gestão de clientes;
- Cadastro e gestão de veículos;
- Cadastro e gestão de serviços;
- Controle de estoque de peças e insumos;
- Controle de reservas de estoque;
- Abertura e gerenciamento de ordens de serviço;
- Processo de diagnóstico técnico;
- Aprovação ou reprovação de orçamento pelo cliente;
- Execução e finalização de serviços;
- Controle de acesso baseado em perfis;
- Registro de indicadores operacionais;
- Monitoramento de qualidade e segurança do código.

---

# 🛠 Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- H2 Database
- Flyway

## Documentação

- Swagger/OpenAPI
- Structurizr DSL
- Domain Storytelling

## Qualidade

- JUnit 5
- Mockito
- JaCoCo
- SonarCloud

## Segurança

- Spring Security
- JWT
- OWASP Dependency Check
- Dependabot
- Snyk

## DevOps

- Docker
- Docker Compose
- GitHub Actions

---

# 🏗 Arquitetura da Solução

A aplicação segue uma arquitetura monolítica em camadas.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Complementando a arquitetura:

```text
DTOs
Mappers
Validators
Factories
Security
Exception Handlers
Filters
```

## Modelo C4

A arquitetura foi documentada utilizando o modelo C4.

### Nível 1 - Contexto

Apresenta os usuários que interagem com a solução e a visão macro do sistema.

### Nível 2 - Containers

Apresenta a divisão lógica da aplicação em seus principais containers tecnológicos.

### Nível 3 - Componentes

Apresenta a estrutura interna da API e seus principais componentes.

📄 Documentação completa:

```text
docs/ARQUITETURA.md
```

---

# 📂 Estrutura do Projeto

```text
src
├── main
│   ├── java
│   │   └── com.techchallenger.oficina360
│   │       ├── config
│   │       ├── constants
│   │       ├── controllers
│   │       ├── docs
│   │       ├── dtos
│   │       ├── entities
│   │       ├── enums
│   │       ├── exceptions
│   │       ├── mappers
│   │       ├── repositories
│   │       ├── security
│   │       ├── services
│   │       ├── utils
│   │       └── validators
│   │
│   └── resources
│       ├── application.yml
│       └── db
│           └── migration
│
└── test
    └── java
        └── com.techchallenger.oficina360
```

---

# 🔐 Segurança

A aplicação utiliza autenticação baseada em JWT.

## Fluxo de Autenticação

1. O usuário realiza login utilizando e-mail e senha;
2. A API valida as credenciais;
3. Um token JWT é gerado;
4. O cliente utiliza o token para acessar recursos protegidos.

Header esperado:

```http
Authorization: Bearer <token>
```

## Perfis

### ADMIN

Possui acesso total à aplicação.

### CLIENTE

Possui acesso apenas aos próprios recursos.

### ATENDENTE

Responsável pelo atendimento e gerenciamento operacional.

### MECANICO

Responsável pelos diagnósticos e execução dos serviços.

---

## Autorização

Além do controle por perfil, a aplicação possui regras de autorização para garantir que um cliente consiga acessar apenas informações associadas à sua conta.

### Exemplos

✅ Consultar sua própria ordem de serviço

✅ Consultar seus próprios veículos

✅ Aprovar sua própria OS

❌ Consultar dados de outro cliente

❌ Aprovar orçamento de outro cliente

---

## Ferramentas de Segurança

- Spring Security;
- JWT;
- OWASP Dependency Check;
- Dependabot;
- SonarCloud;
- Snyk.

Objetivos:

- Identificação de vulnerabilidades;
- Proteção de dependências;
- Controle de acesso;
- Segurança contínua da aplicação.

---

# 👥 Módulos Disponíveis

## Clientes

- Cadastrar cliente;
- Listar clientes;
- Buscar cliente por documento;
- Atualizar cliente;
- Excluir cliente.

## Veículos

- Cadastrar veículo;
- Listar veículos;
- Buscar por placa;
- Atualizar veículo;
- Excluir veículo;
- Vincular veículo ao cliente.

## Serviços

- Cadastrar serviço;
- Listar serviços;
- Buscar serviço;
- Atualizar serviço;
- Excluir serviço;
- Calcular tempo médio de execução.

## Estoque

- Cadastrar item;
- Listar itens;
- Buscar item;
- Atualizar item;
- Excluir item;
- Reservar quantidade;
- Controlar disponibilidade.

## Ordens de Serviço

- Abrir ordem de serviço;
- Consultar ordem de serviço;
- Executar diagnóstico;
- Aprovar orçamento;
- Reprovar orçamento;
- Iniciar execução;
- Finalizar execução;
- Registrar indicadores operacionais.

---

# 🔄 Fluxo da Ordem de Serviço

```text
RECEBIDA
    ↓
EM_DIAGNOSTICO
    ↓
AGUARDANDO_APROVACAO
   ↙          ↘
REPROVADA   APROVADA
               ↓
         EM_EXECUCAO
               ↓
          FINALIZADA
```

---

# 📦 Controle de Estoque

Durante o diagnóstico:

- Serviços podem ser adicionados à ordem;
- Peças e insumos podem ser associados;
- Itens do estoque são reservados automaticamente.

Exemplo:

```text
Quantidade Total: 20
Reservados: 5
Disponíveis: 15
```

Fórmula:

```text
Disponíveis = Quantidade - Reservados
```

---

# 📈 Indicadores Operacionais

Ao finalizar uma ordem de serviço:

- O tempo total de execução é calculado;
- O histórico é armazenado;
- O sistema calcula automaticamente o tempo médio dos serviços executados.

Esses dados auxiliam:

- Planejamento operacional;
- Controle de produtividade;
- Estimativas futuras.

---

# 📊 Observabilidade

A aplicação possui log estruturado de requisições e respostas HTTP.

Informações registradas:

- Request ID;
- Método HTTP;
- URI;
- Usuário autenticado;
- Tempo de resposta;
- Status HTTP;
- Payload sanitizado.

## Proteção de Dados Sensíveis

Campos protegidos:

- CPF;
- CNPJ;
- Senhas;
- Tokens JWT.

Exemplo:

```json
{
  "senha": "***"
}
```

---

# 🗄 Banco de Dados

Banco principal:

```text
PostgreSQL
```

Banco para testes e desenvolvimento:

```text
H2 Database
```

## Migrações

Controladas através do Flyway.

Localização:

```text
src/main/resources/db/migration
```

---

# 📚 Documentação da API

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Docker:

```text
http://localhost:18080/swagger-ui/index.html
```

A documentação inclui:

- Endpoints;
- DTOs;
- Segurança JWT;
- Exemplos de Payloads;
- Códigos HTTP.

---

# 🚀 Executando Localmente

## Pré-requisitos

- Java 21
- Maven
- Docker
- Docker Compose

## Clone do Projeto

```bash
git clone https://github.com/michaelhion/tech_challenge_pos_arquitetura_de_software.git
```

```bash
cd tech_challenge_pos_arquitetura_de_software
```

---

## Executar com Maven

Linux/Mac:

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

---

## Executar com Docker

```bash
chmod +x start.sh
./start.sh
```

---

# 🔑 Login

Endpoint:

```http
POST /auth/login
```

Administrador:

```json
{
  "email": "admin@oficina360.com",
  "senha": "123456"
}
```

Cliente:

```json
{
  "email": "cliente@oficina360.com",
  "senha": "123456"
}
```

---

# 🧪 Testes

Executar os testes:

```bash
./mvnw test
```

Executar build completo:

```bash
./mvnw clean verify
```

## Cobertura

Monitorada por:

- JaCoCo;
- SonarCloud.

Objetivo:

```text
Cobertura mínima de 80%
```

---

# 🔍 Qualidade e Segurança

## Ferramentas Utilizadas

- SonarCloud;
- JaCoCo;
- OWASP Dependency Check;
- Dependabot;
- Snyk;
- GitHub Actions.

## Métricas Monitoradas

- Bugs;
- Vulnerabilidades;
- Security Hotspots;
- Cobertura de Testes;
- Code Smells;
- Duplicação de Código;
- Manutenibilidade.

---

# 🔄 Integração Contínua

O GitHub Actions executa automaticamente:

```text
Build
↓
Testes
↓
JaCoCo
↓
Dependency Check
↓
SonarCloud
```

A cada push e pull request.

Dashboard SonarCloud:

🔗 https://sonarcloud.io/summary/overall?id=michaelhion_tech_challenge_pos_arquitetura_de_software

---

# 🔒 Relatórios de Segurança

Os relatórios de segurança encontram-se em:

```text
docs/security/
```

Incluindo:

- OWASP Dependency Check;
- Relatórios de Vulnerabilidades;
- Evidências de Segurança.

---

# 📖 Documentação Complementar

| Documento | Localização |
|------------|------------|
| Arquitetura | docs/ARQUITETURA.md |
| Requisitos | docs/requisitos/Documento_Requisitos_Oficina360.md |
| Linguagem Ubíqua | docs/requisitos/LINGUAGEM-UBIQUA.md |
| Domain Storytelling | docs/ddd |
| Diagramas C4 | docs/c4 |
| Relatórios de Segurança | docs/security |

---

# ⚠ Tratamento de Erros

A API utiliza tratamento global de exceções.

Principais exceções:

- RecursoNaoEncontradoException;
- RegraDeNegocioException;
- ConflitoException;
- AccessDeniedException.

Exemplo:

```json
{
  "status": 404,
  "erro": "Recurso não encontrado",
  "mensagem": "Cliente não encontrado"
}
```

---

# ✅ Funcionalidades Implementadas

- [x] CRUD de Clientes
- [x] CRUD de Veículos
- [x] CRUD de Serviços
- [x] CRUD de Estoque
- [x] Reserva de Estoque
- [x] Autenticação JWT
- [x] Controle de Acesso por Perfil
- [x] Controle de Propriedade
- [x] Ordem de Serviço
- [x] Diagnóstico Técnico
- [x] Aprovação de Orçamento
- [x] Execução de Serviços
- [x] Cálculo de Tempo Médio
- [x] Flyway
- [x] Swagger/OpenAPI
- [x] Docker
- [x] Testes Automatizados
- [x] JaCoCo
- [x] SonarCloud
- [x] OWASP Dependency Check
- [x] Dependabot
- [x] Snyk

---

# 👨‍💻 Autor

Projeto desenvolvido para fins acadêmicos no contexto do Tech Challenge da Pós-Graduação em Arquitetura de Software.