-- Dados básicos para o sistema CRM Shot Fair Brasil
-- Execute após a criação das tabelas pelo JPA

-- Inserir apenas usuários básicos necessários para o sistema funcionar
INSERT INTO usuarios (nome, email, senha, perfil, ativo, data_criacao) VALUES
('Administrador', 'admin@crmshot.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMINISTRADOR', true, NOW());

-- Comentários sobre as tabelas
COMMENT ON TABLE usuarios IS 'Tabela de usuários do sistema';
COMMENT ON TABLE expositores IS 'Tabela de expositores/clientes';
COMMENT ON TABLE oportunidades IS 'Tabela de oportunidades de vendas';
COMMENT ON TABLE interacoes IS 'Tabela de histórico de interações';
