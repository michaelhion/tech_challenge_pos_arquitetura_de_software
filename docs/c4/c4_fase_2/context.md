```mermaid
flowchart LR

    Cliente[Cliente]
    Atendente[Atendente]
    Mecanico[Mecânico]
    Admin[Administrador]

    Sistema[Oficina360]

    Cliente -->|Consulta status da OS\nAprova orçamento| Sistema

    Atendente -->|Gerencia clientes\nveículos e OS| Sistema

    Mecanico -->|Realiza diagnósticos\nExecuta serviços| Sistema

    Admin -->|Administra usuários\nConfigura sistema| Sistema
````