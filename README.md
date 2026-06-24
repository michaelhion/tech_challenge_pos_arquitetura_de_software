# Oficina360 API

## 📖 Sobre o projeto

O **Oficina360** é uma API REST desenvolvida como parte do Tech Challenge da Pós-Graduação em Arquitetura de Software.

O objetivo da aplicação é fornecer uma solução completa para o gerenciamento de oficinas mecânicas, contemplando desde o cadastro de clientes e veículos até o gerenciamento completo de ordens de serviço, controle de estoque, autenticação de usuários, autorização de acesso e indicadores operacionais.

O projeto foi desenvolvido seguindo princípios de arquitetura em camadas, boas práticas de engenharia de software, segurança, observabilidade, testes automatizados e integração contínua.

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

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- H2 Database
- Flyway
- Swagger/OpenAPI
- Lombok
- Maven
- Docker
- Docker Compose
- JUnit 5
- Mockito
- JaCoCo
- SonarCloud
- OWASP Dependency Check
- GitHub Actions

---

# 🏗 Arquitetura

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

## Fluxo de autenticação

1. O usuário realiza login utilizando e-mail e senha;
2. A API valida as credenciais;
3. Um token JWT é gerado;
4. O cliente utiliza o token para acessar recursos protegidos.

Header esperado:

```http
Authorization: Bearer <token>
```

---

## Perfis

A aplicação suporta os seguintes perfis:

### ADMIN

Possui acesso total à aplicação.

Responsável por:

- Gerenciar clientes;
- Gerenciar veículos;
- Gerenciar serviços;
- Gerenciar estoque;
- Gerenciar ordens de serviço;
- Executar diagnósticos;
- Iniciar e finalizar execuções.

### CLIENTE

Possui acesso limitado aos próprios recursos.

Pode:

- Consultar seus dados;
- Consultar seus veículos;
- Consultar suas ordens de serviço;
- Aprovar orçamentos;
- Reprovar orçamentos.

---

## Autorização

Além do controle por perfil, a aplicação possui regras de autorização para garantir que um cliente consiga acessar apenas informações associadas ao seu documento.

Exemplos:

✅ Consultar sua própria ordem de serviço

✅ Consultar seus próprios veículos

✅ Aprovar sua própria OS

❌ Visualizar dados de outro cliente

❌ Aprovar orçamento de outro cliente

---

# 👥 Módulos Disponíveis

## Clientes

- Cadastrar cliente;
- Listar clientes;
- Buscar cliente por documento;
- Atualizar cliente;
- Excluir cliente.

---

## Veículos

- Cadastrar veículo;
- Listar veículos;
- Buscar por placa;
- Atualizar veículo;
- Excluir veículo;
- Vincular veículo ao cliente.

---

## Serviços

- Cadastrar serviço;
- Listar serviços;
- Buscar serviço;
- Atualizar serviço;
- Excluir serviço;
- Calcular tempo médio de execução.

---

## Estoque

- Cadastrar item;
- Listar itens;
- Buscar item;
- Atualizar item;
- Excluir item;
- Reservar quantidade;
- Controlar disponibilidade.

---

## Ordens de Serviço

- Abrir ordem de serviço;
- Consultar ordem de serviço;
- Atualizar descrição do problema;
- Executar diagnóstico;
- Associar serviços;
- Associar peças e insumos;
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
disponíveis = quantidade - reservados
```

---

# 📈 Indicadores Operacionais

Ao finalizar uma ordem de serviço:

- O tempo total de execução é calculado;
- O tempo é distribuído entre os serviços executados;
- O histórico é armazenado;
- O sistema calcula o tempo médio de execução dos serviços.

Esses indicadores podem ser utilizados para:

- Planejamento operacional;
- Estimativas futuras;
- Análise de produtividade.

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

---

## Proteção de dados sensíveis

O filtro de logs realiza mascaramento automático de:

- CPF;
- CNPJ;
- Senhas;
- Tokens JWT.

Exemplo:

```json
{
  "senha":"***"
}
```

---

# 🗄 Banco de Dados

O projeto utiliza PostgreSQL como banco principal.

Para testes e desenvolvimento:

```text
H2 Database
```

gerenciado automaticamente pelo Spring Boot.

---

## Migrações

Todas as alterações de banco são controladas via Flyway.

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

Caso esteja rodando no docker:

```text
http://localhost:18080/swagger-ui/index.html
```

A documentação inclui:

- Endpoints;
- DTOs;
- Segurança JWT;
- Exemplos de payload;
- Códigos HTTP.

---

# 🚀 Executando Localmente

## Pré-requisitos

- Java 21
- Maven
- Docker
- Docker Compose

---

## Clone do Projeto

```bash
git clone https://github.com/michaelhion/tech_challenge_pos_arquitetura_de_software.git
```

```bash
cd oficina360
```
---

### Ambiente Linux

O projeto pode ser executado nativamente em distribuições Linux compatíveis.

### Ambiente Windows

Para execução utilizando os scripts Linux (`start.sh`), recomenda-se:

- WSL 2 (Windows Subsystem for Linux);

ou

- Docker Desktop instalado e configurado com suporte ao WSL 2.

Caso utilize apenas Docker Desktop, certifique-se de que a virtualização e a integração com WSL estejam habilitadas.

### Verificando a instalação

Verifique se o Docker está disponível:

```bash
docker --version
```
```bash
docker compose version
````
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

Windows:

```cmd
start.bat
```

Linux:

```bash
chmod +x start.sh
```
```bash
./start.sh
````

Parar containers:

```bash
docker compose down
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

Executar todos os testes:

```bash
./mvnw test
```

Windows:

```cmd
mvnw.cmd test
```

Executar build completo:

```bash
./mvnw clean verify
```

---

## Cobertura

O projeto possui testes para:

- Services;
- Controllers;
- Security;
- Exception Handlers;
- Filtros HTTP;
- Regras de negócio;
- Fluxos de ordens de serviço.

Métricas monitoradas por:

- JaCoCo;
- SonarCloud.

Objetivo:

```text
Cobertura mínima de 80%
```

---

# 🔍 Qualidade e Segurança

A aplicação utiliza:

- SonarCloud;
- JaCoCo;
- OWASP Dependency Check;
- GitHub Actions.

Os seguintes aspectos são monitorados:

- Cobertura de testes;
- Vulnerabilidades;
- Code Smells;
- Duplicação de código;
- Segurança;
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

a cada push e pull request.


Dashboard SonarCloud:

🔗 https://sonarcloud.io/summary/overall?id=michaelhion_tech_challenge_pos_arquitetura_de_software

---

---
## 🔒 Relatório de Vulnerabilidades

O relatório de vulnerabilidades encontra-se em:

docs/security/OWASP-Dependency-Check.pd
---

# ⚠ Tratamento de Erros

A API utiliza tratamento global de exceções.

Principais exceções:

- RecursoNaoEncontradoException
- RegraDeNegocioException
- ConflitoException
- AccessDeniedException

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

- [x] CRUD de clientes
- [x] CRUD de veículos
- [x] CRUD de serviços
- [x] CRUD de estoque
- [x] Reserva de estoque
- [x] Autenticação JWT
- [x] Controle de acesso por perfil
- [x] Controle de acesso por proprietário
- [x] Abertura de ordem de serviço
- [x] Diagnóstico técnico
- [x] Aprovação de orçamento
- [x] Reprovação de orçamento
- [x] Execução de serviços
- [x] Finalização de ordens de serviço
- [x] Cálculo de tempo médio de execução
- [x] Swagger/OpenAPI
- [x] Flyway
- [x] Docker
- [x] Testes automatizados
- [x] JaCoCo
- [x] SonarCloud
- [x] OWASP Dependency Check

---

# 👨‍💻 Autor

Projeto desenvolvido para fins acadêmicos no contexto do Tech Challenge da Pós-Graduação em Arquitetura de Software.
