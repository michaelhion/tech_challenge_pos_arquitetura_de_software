workspace {

    model {

        usuarioSistema = person "Usuário"

        oficina360 = softwareSystem "Oficina360" {

            api = container "API Oficina360" "Backend da aplicação" "Spring Boot" {

                authController = component "AuthController"
                clienteController = component "ClienteController"
                ordemServicoController = component "OrdemServicoController"

                authService = component "AuthService"
                clienteService = component "ClienteService"
                ordemServicoService = component "OrdemServicoService"

                jwtService = component "JwtService"

                clienteRepository = component "ClienteRepository"
                ordemServicoRepository = component "OrdemServicoRepository"

                exceptionHandler = component "GlobalExceptionHandler"

                usuarioSistema -> authController "Autentica"
                usuarioSistema -> clienteController "Gerencia clientes"
                usuarioSistema -> ordemServicoController "Gerencia OS"

                authController -> authService
                clienteController -> clienteService
                ordemServicoController -> ordemServicoService

                authService -> jwtService

                clienteService -> clienteRepository
                ordemServicoService -> ordemServicoRepository

                clienteController -> exceptionHandler
                ordemServicoController -> exceptionHandler
            }
        }
    }

    views {

        component api {
            include *
            autolayout lr
        }

        styles {

            element "Person" {
                shape Person
                background #08427b
                color #ffffff
            }

            element "Component" {
                background #85bbf0
                color #000000
            }
        }
    }
}