INSERT INTO SERVICO (
    ID,
    CODIGO,
    DESCRICAO,
    VALOR

)values
(
    '27b91d49-46dd-41df-a6c1-3b2e7d53e677',
    'ALINHAMENTO',
    'Alinhamento',
    120.00
),
(
    'ef633729-72b4-4016-883a-1dd9803d0652',
    'BALANCEAMENTO',
    'Balanceamento',
    100.00
),
(
    '5eb13243-9469-49fb-854f-ebaaee300b6e',
    'DIAGNOSTICO-ELETRICO',
    'Diagnóstico elétrico',
    150.00
),
(
    '31edeb2f-d78a-4e6b-921a-4f66f80ae9b6',
    'REVISAO-DE-FREIOS',
    'Revisão de freios',
    180.00
),
(
    'd560b0f9-0515-4827-a6a5-e172bff0455e',
    'TROCA-DE-BATERIA',
    'Troca de bateria',
    70.00
),
(
    '7a65bb0e-419d-4f58-951b-b74c29808aaf',
    'REVISAO-PREVENCIONAL',
    'Revisão preventiva',
    250.00
),
(
    'acae48b0-31b9-4159-85c6-eb65d47751d8',
    'TROCA-DE-FILTRO-DE-AR',
    'Troca de filtro de ar',
    50.00
);

INSERT INTO CLIENTE (
    ID,
    DOCUMENTO,
    NOME,
    EMAIL,
    TELEFONE
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          '12345678901',
          'João da Silva',
          'joao.silva@email.com',
          '11999990001'
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          '98765432100',
          'Maria Oliveira',
          'maria.oliveira@email.com',
          '11999990002'
      ),
      (
          '33333333-3333-3333-3333-333333333333',
          '11222333000181',
          'Oficina Parceira LTDA',
          'contato@oficinaparceira.com',
          '1133334444'
      );

INSERT INTO VEICULO (
    ID,
    PLACA,
    MARCA,
    MODELO,
    ANO,
    CLIENTE_DOCUMENTO
) VALUES
  (
      'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
      'ABC1D23',
      'Volkswagen',
      'Gol',
      '2020',
      '12345678901'
  ),
  (
      'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
      'DEF2G34',
      'Toyota',
      'Corolla',
      '2022',
      '98765432100'
  ),
  (
      'cccccccc-cccc-cccc-cccc-cccccccccccc',
      'GHI3J45',
      'Fiat',
      'Toro',
      '2021',
      '11222333000181'
  );

INSERT INTO ESTOQUE(
    ID,
    NOME,
    VALOR,
    QUANTIDADE,
    RESERVADOS,
    CODIGO
) values (
    '2889afd4-ea80-4fde-b7eb-59282be979ff',
    'Pneu 205/55 R16',
    400.00,
    20,
    1,
    'PNEU-205-55-R16'
),
(
    'd1c33abb-5239-42ce-ae77-570fed72e92f',
    'Óleo de motor 5W30',
    150.00,
    50,
    3,
    'OLEO-5W30'
),
(
    '315c17bb-7d61-4898-a1df-11d872c182cd',
    'Filtro de ar',
    80.00,
    30,
    2,
    'FILTRO-DE-AR'
);

INSERT INTO ORDEM_SERVICO (
    ID,
    DOCUMENTO_CLIENTE,
    PLACA_VEICULO,
    DT_HORA_ABERTURA,
    DT_HORA_FECHAMENTO,
    DESCRICAO_PROBLEMA,
    ORDEM_DE_SERVICO_STATUS,
    VALOR_SERVICOS,
    VALOR_PECAS_INSUMOS,
    VALOR_OS
) VALUES (
    '7b5a3247-a14a-44f8-872f-016e179a92fd',
    '12345678901',
    'ABC1D23',
    CURRENT_TIMESTAMP,
    NULL,
    'Veículo apresenta ruído ao frear e vibração no volante.',
    'RECEBIDA',
    0.00,
    0.00,
    0.00
);


INSERT INTO TEMPO_EXECUCAO_SERVICO (
    ID,
    SERVICO_ID,
    TEMPO_EXECUCAO_MINUTOS,
    DATA_EXECUCAO
) VALUES

-- ALINHAMENTO (média = 30)
('11111111-1111-1111-1111-111111111111',
 '27b91d49-46dd-41df-a6c1-3b2e7d53e677',
 20,
 TIMESTAMP '2025-01-10 08:00:00'),

('11111111-1111-1111-1111-111111111112',
 '27b91d49-46dd-41df-a6c1-3b2e7d53e677',
 30,
 TIMESTAMP '2025-01-15 08:00:00'),

('11111111-1111-1111-1111-111111111113',
 '27b91d49-46dd-41df-a6c1-3b2e7d53e677',
 40,
 TIMESTAMP '2025-01-20 08:00:00'),

-- BALANCEAMENTO (média = 25)
('22222222-2222-2222-2222-222222222221',
 'ef633729-72b4-4016-883a-1dd9803d0652',
 20,
 TIMESTAMP '2025-01-12 08:00:00'),

('22222222-2222-2222-2222-222222222222',
 'ef633729-72b4-4016-883a-1dd9803d0652',
 25,
 TIMESTAMP '2025-01-16 08:00:00'),

('22222222-2222-2222-2222-222222222223',
 'ef633729-72b4-4016-883a-1dd9803d0652',
 30,
 TIMESTAMP '2025-01-18 08:00:00'),

-- DIAGNOSTICO ELETRICO (média = 65)
('33333333-3333-3333-3333-333333333331',
 '5eb13243-9469-49fb-854f-ebaaee300b6e',
 60,
 TIMESTAMP '2025-01-14 08:00:00'),

('33333333-3333-3333-3333-333333333332',
 '5eb13243-9469-49fb-854f-ebaaee300b6e',
 70,
 TIMESTAMP '2025-01-17 08:00:00'),

-- REVISAO FREIOS (média = 90)
('44444444-4444-4444-4444-444444444441',
 '31edeb2f-d78a-4e6b-921a-4f66f80ae9b6',
 80,
 TIMESTAMP '2025-01-13 08:00:00'),

('44444444-4444-4444-4444-444444444442',
 '31edeb2f-d78a-4e6b-921a-4f66f80ae9b6',
 100,
 TIMESTAMP '2025-01-19 08:00:00'),

-- TROCA BATERIA (média = 15)
('55555555-5555-5555-5555-555555555551',
 'd560b0f9-0515-4827-a6a5-e172bff0455e',
 10,
 TIMESTAMP '2025-01-15 08:00:00'),

('55555555-5555-5555-5555-555555555552',
 'd560b0f9-0515-4827-a6a5-e172bff0455e',
 20,
 TIMESTAMP '2025-01-18 08:00:00'),

-- REVISAO PREVENTIVA (média = 120)
('66666666-6666-6666-6666-666666666661',
 '7a65bb0e-419d-4f58-951b-b74c29808aaf',
 100,
 TIMESTAMP '2025-01-11 08:00:00'),

('66666666-6666-6666-6666-666666666662',
 '7a65bb0e-419d-4f58-951b-b74c29808aaf',
 120,
 TIMESTAMP '2025-01-16 08:00:00'),

('66666666-6666-6666-6666-666666666663',
 '7a65bb0e-419d-4f58-951b-b74c29808aaf',
 140,
 TIMESTAMP '2025-01-20 08:00:00'),

-- TROCA FILTRO AR (média = 18)
('77777777-7777-7777-7777-777777777771',
 'acae48b0-31b9-4159-85c6-eb65d47751d8',
 15,
 TIMESTAMP '2025-01-17 08:00:00'),

('77777777-7777-7777-7777-777777777772',
 'acae48b0-31b9-4159-85c6-eb65d47751d8',
 18,
 TIMESTAMP '2025-01-18 08:00:00'),

('77777777-7777-7777-7777-777777777773',
 'acae48b0-31b9-4159-85c6-eb65d47751d8',
 21,
 TIMESTAMP '2025-01-19 08:00:00');


