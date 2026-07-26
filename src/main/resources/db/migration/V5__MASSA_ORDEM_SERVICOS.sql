-- V9002__insert_20_ordens_servico_paginacao.sql
-- Massa de dados para estudar paginacao e ordenacao.
-- IDs fixos e datas distintas facilitam validar a ordem dos resultados.
-- Ajuste os documentos e placas caso existam constraints/FKs adicionais no seu schema.

INSERT INTO ORDEM_SERVICO (
    ID,
    DOCUMENTO_CLIENTE,
    PLACA_VEICULO,
    DT_HORA_ABERTURA,
    DT_HORA_FECHAMENTO,
    DT_HORA_INICIO_EXECUCAO,
    DT_HORA_FIM_EXECUCAO,
    DESCRICAO_PROBLEMA,
    ORDEM_DE_SERVICO_STATUS,
    VALOR_SERVICOS,
    VALOR_PECAS_INSUMOS,
    VALOR_OS,
    OBSERVACAO_CLIENTE
) VALUES
-- 01 a 04: RECEBIDA
(
    '20000000-0000-0000-0000-000000000001',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-01 08:00:00', NULL, NULL, NULL,
    'Ruido ao frear em baixa velocidade.',
    'RECEBIDA', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000002',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-02 09:00:00', NULL, NULL, NULL,
    'Vibracao no volante acima de 80 km por hora.',
    'RECEBIDA', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000003',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-03 10:00:00', NULL, NULL, NULL,
    'Luz de injecao acesa no painel.',
    'RECEBIDA', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000004',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-04 11:00:00', NULL, NULL, NULL,
    'Ar-condicionado nao esta resfriando.',
    'RECEBIDA', 0.00, 0.00, 0.00, NULL
),

-- 05 a 08: EM_DIAGNOSTICO
(
    '20000000-0000-0000-0000-000000000005',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-05 08:30:00', NULL, NULL, NULL,
    'Barulho metalico na suspensao dianteira.',
    'EM_DIAGNOSTICO', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000006',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-06 09:30:00', NULL, NULL, NULL,
    'Motor apresenta perda de potencia.',
    'EM_DIAGNOSTICO', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000007',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-07 10:30:00', NULL, NULL, NULL,
    'Dificuldade para dar partida pela manha.',
    'EM_DIAGNOSTICO', 0.00, 0.00, 0.00, NULL
),
(
    '20000000-0000-0000-0000-000000000008',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-08 11:30:00', NULL, NULL, NULL,
    'Consumo de combustivel acima do normal.',
    'EM_DIAGNOSTICO', 0.00, 0.00, 0.00, NULL
),

-- 09 a 12: AGUARDANDO_APROVACAO
(
    '20000000-0000-0000-0000-000000000009',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-09 08:00:00', NULL, NULL, NULL,
    'Pneus apresentam desgaste irregular.',
    'AGUARDANDO_APROVACAO', 120.00, 800.00, 920.00, NULL
),
(
    '20000000-0000-0000-0000-000000000010',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-10 09:00:00', NULL, NULL, NULL,
    'Pastilhas de freio com desgaste.',
    'AGUARDANDO_APROVACAO', 180.00, 300.00, 480.00, NULL
),
(
    '20000000-0000-0000-0000-000000000011',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-11 10:00:00', NULL, NULL, NULL,
    'Bateria descarregando rapidamente.',
    'AGUARDANDO_APROVACAO', 70.00, 650.00, 720.00, NULL
),
(
    '20000000-0000-0000-0000-000000000012',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-12 11:00:00', NULL, NULL, NULL,
    'Necessidade de revisao preventiva.',
    'AGUARDANDO_APROVACAO', 250.00, 230.00, 480.00, NULL
),

-- 13 a 15: ORCAMENTO_APROVADO
(
    '20000000-0000-0000-0000-000000000013',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-13 08:00:00', NULL, NULL, NULL,
    'Troca do filtro de ar.',
    'ORCAMENTO_APROVADO', 50.00, 80.00, 130.00,
    'Orcamento aprovado pelo cliente.'
),
(
    '20000000-0000-0000-0000-000000000014',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-14 09:00:00', NULL, NULL, NULL,
    'Alinhamento e balanceamento necessarios.',
    'ORCAMENTO_APROVADO', 220.00, 0.00, 220.00,
    'Orcamento aprovado pelo cliente.'
),
(
    '20000000-0000-0000-0000-000000000015',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-15 10:00:00', NULL, NULL, NULL,
    'Falha no sistema eletrico.',
    'ORCAMENTO_APROVADO', 150.00, 200.00, 350.00,
    'Orcamento aprovado pelo cliente.'
),

-- 16 e 17: EM_EXECUCAO
(
    '20000000-0000-0000-0000-000000000016',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-07-16 08:00:00', NULL,
    TIMESTAMP '2026-07-20 08:30:00', NULL,
    'Revisao do sistema de freios em execucao.',
    'EM_EXECUCAO', 180.00, 300.00, 480.00,
    'Orcamento aprovado e servico iniciado.'
),
(
    '20000000-0000-0000-0000-000000000017',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-07-17 09:00:00', NULL,
    TIMESTAMP '2026-07-20 09:30:00', NULL,
    'Troca de bateria em execucao.',
    'EM_EXECUCAO', 70.00, 650.00, 720.00,
    'Orcamento aprovado e servico iniciado.'
),

-- 18: ORCAMENTO_REPROVADO
(
    '20000000-0000-0000-0000-000000000018',
    '21222333000181', 'GHI3J45',
    TIMESTAMP '2026-07-18 10:00:00',
    TIMESTAMP '2026-07-19 10:00:00', NULL, NULL,
    'Reparo recusado pelo cliente.',
    'ORCAMENTO_REPROVADO', 120.00, 800.00, 920.00,
    'Cliente recusou o orcamento.'
),

-- 19 e 20: estados encerrados, uteis para testar exclusao da listagem operacional
(
    '20000000-0000-0000-0000-000000000019',
    '22345678901', 'ABC1D23',
    TIMESTAMP '2026-06-20 08:00:00',
    TIMESTAMP '2026-06-21 12:00:00',
    TIMESTAMP '2026-06-21 09:00:00',
    TIMESTAMP '2026-06-21 11:30:00',
    'Servico de freios concluido.',
    'FINALIZADA', 180.00, 300.00, 480.00,
    'Servico finalizado.'
),
(
    '20000000-0000-0000-0000-000000000020',
    '08765432100', 'DEF2G34',
    TIMESTAMP '2026-06-22 08:00:00',
    TIMESTAMP '2026-06-23 14:00:00',
    TIMESTAMP '2026-06-23 09:00:00',
    TIMESTAMP '2026-06-23 13:30:00',
    'Veiculo reparado e entregue ao cliente.',
    'ENTREGUE', 250.00, 230.00, 480.00,
    'Veiculo entregue ao cliente.'
);
