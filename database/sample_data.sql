-- Dados de exemplo para o sistema CRM Shot Fair Brasil
-- Execute após a criação das tabelas pelo JPA

-- Inserir usuários de exemplo
INSERT INTO usuarios (nome, email, senha, perfil, ativo, data_criacao) VALUES
('Administrador', 'admin@crmshot.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMINISTRADOR', true, NOW()),
('João Silva', 'joao@crmshot.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'VENDEDOR', true, NOW()),
('Maria Santos', 'maria@crmshot.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'VENDEDOR', true, NOW()),
('Pedro Costa', 'pedro@crmshot.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'GERENTE', true, NOW());

-- Inserir expositores de exemplo
INSERT INTO expositores (razao_social, nome_fantasia, cnpj, email, telefone, celular, endereco, cidade, estado, cep, site, descricao, status, data_cadastro, vendedor_id) VALUES
('Tech Solutions LTDA', 'TechSol', '12.345.678/0001-90', 'contato@techsol.com.br', '(11) 3333-4444', '(11) 99999-8888', 'Rua das Flores, 123', 'São Paulo', 'SP', '01234-567', 'www.techsol.com.br', 'Empresa de tecnologia especializada em soluções corporativas', 'ATIVO', NOW(), 2),
('Inovação Digital S/A', 'InovaDig', '98.765.432/0001-10', 'vendas@inovadig.com.br', '(21) 2222-3333', '(21) 88888-7777', 'Av. Brasil, 456', 'Rio de Janeiro', 'RJ', '20000-000', 'www.inovadig.com.br', 'Consultoria em transformação digital', 'ATIVO', NOW(), 2),
('StartupTech ME', 'StartupTech', '11.222.333/0001-44', 'info@startuptech.com', '(31) 1111-2222', '(31) 77777-6666', 'Rua da Inovação, 789', 'Belo Horizonte', 'MG', '30000-000', 'www.startuptech.com', 'Startup focada em soluções mobile', 'POTENCIAL', NOW(), 3),
('Consultoria Empresarial LTDA', 'ConsEmp', '55.666.777/0001-88', 'contato@consemp.com.br', '(41) 4444-5555', '(41) 66666-5555', 'Rua Comercial, 321', 'Curitiba', 'PR', '40000-000', 'www.consemp.com.br', 'Consultoria em gestão empresarial', 'ATIVO', NOW(), 3),
('Soluções Web LTDA', 'WebSol', '99.888.999/0001-22', 'vendas@websol.com.br', '(51) 5555-6666', '(51) 55555-4444', 'Av. Tecnologia, 654', 'Porto Alegre', 'RS', '50000-000', 'www.websol.com.br', 'Desenvolvimento de sites e aplicações web', 'INATIVO', NOW(), 2);

-- Inserir oportunidades de exemplo
INSERT INTO oportunidades (titulo, descricao, expositor_id, vendedor_id, status, fonte, valor_estimado, probabilidade_fechamento, data_prevista_fechamento, data_criacao) VALUES
('Implementação de CRM', 'Sistema de CRM completo para gestão de clientes', 1, 2, 'PROSPECCAO', 'SITE', 50000.00, 20, '2024-03-15 00:00:00', NOW()),
('Desenvolvimento de App Mobile', 'Aplicativo mobile para e-commerce', 2, 2, 'QUALIFICACAO', 'INDICACAO', 75000.00, 40, '2024-04-20 00:00:00', NOW()),
('Consultoria em Transformação Digital', 'Projeto de transformação digital completo', 3, 3, 'PROPOSTA', 'EMAIL', 120000.00, 60, '2024-05-10 00:00:00', NOW()),
('Sistema de Gestão', 'Sistema ERP personalizado', 4, 3, 'NEGOCIACAO', 'TELEFONE', 80000.00, 80, '2024-03-30 00:00:00', NOW()),
('Website Corporativo', 'Desenvolvimento de site institucional', 5, 2, 'FECHADA_GANHA', 'SITE', 15000.00, 100, '2024-02-15 00:00:00', NOW());

-- Inserir interações de exemplo
INSERT INTO interacoes (expositor_id, usuario_id, oportunidade_id, tipo, assunto, descricao, data_interacao, data_criacao, proxima_acao, data_proxima_acao, concluida) VALUES
(1, 2, 1, 'LIGACAO', 'Primeiro contato', 'Ligação inicial para apresentar a empresa e entender as necessidades', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 'Enviar proposta comercial', NOW() + INTERVAL '3 days', false),
(2, 2, 2, 'EMAIL', 'Envio de portfólio', 'Enviado portfólio de projetos similares e cases de sucesso', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 'Agendar reunião presencial', NOW() + INTERVAL '1 week', false),
(3, 3, 3, 'REUNIAO', 'Reunião de apresentação', 'Reunião presencial para apresentar a proposta de transformação digital', NOW() - INTERVAL '3 hours', NOW() - INTERVAL '3 hours', 'Enviar proposta detalhada', NOW() + INTERVAL '2 days', false),
(4, 3, 4, 'WHATSAPP', 'Negociação de preço', 'Conversa via WhatsApp sobre valores e condições de pagamento', NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour', 'Enviar contrato para assinatura', NOW() + INTERVAL '1 day', false),
(5, 2, 5, 'EMAIL', 'Projeto finalizado', 'Projeto concluído com sucesso e entregue ao cliente', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', 'Solicitar depoimento', NOW() + INTERVAL '1 month', true);

-- Comentários sobre os dados
COMMENT ON TABLE usuarios IS 'Tabela de usuários do sistema';
COMMENT ON TABLE expositores IS 'Tabela de expositores/clientes';
COMMENT ON TABLE oportunidades IS 'Tabela de oportunidades de vendas';
COMMENT ON TABLE interacoes IS 'Tabela de histórico de interações';
