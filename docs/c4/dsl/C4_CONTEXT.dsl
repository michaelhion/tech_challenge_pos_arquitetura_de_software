workspace {

    model {

        cliente = person "Cliente"
        atendente = person "Atendente"
        mecanico = person "Mecânico"
        admin = person "Administrador"

        oficina360 = softwareSystem "Oficina360" {
            description "Sistema Integrado de Atendimento e Execução de Serviços"
        }

        cliente -> oficina360 "Consulta OS e aprova orçamento"
        atendente -> oficina360 "Gerencia clientes, veículos e OS"
        mecanico -> oficina360 "Executa diagnósticos e serviços"
        admin -> oficina360 "Administra o sistema"
    }

    views {

        systemContext oficina360 {
            include *
            autolayout lr
        }

        theme default
    }
}