    workspace {

        model {

            cliente = person "Cliente"

            oficina360 = softwareSystem "Oficina360" {


            api = container "API Oficina360" "API REST responsável pela gestão de clientes, veículos, estoque, serviços e ordens de serviço, incluindo autenticação JWT." "Spring Boot"

            banco = container "Banco de Dados" "Persistência de dados da aplicação." "PostgreSQL"

            swagger = container "Swagger/OpenAPI" "Documentação interativa da API." "SpringDoc"

            }

            cliente -> api "HTTPS + JWT"

            api -> banco "JPA/Hibernate"

            swagger -> api "Documenta os endpoints"
        }

        views {

            container oficina360 {
                include *
                autolayout lr
            }

            theme default
        }
    }