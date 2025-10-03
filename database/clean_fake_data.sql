-- Script para remover dados fictícios do banco de dados
-- Execute este script para limpar empresas fictícias

-- Remover interações relacionadas às empresas fictícias
DELETE FROM interacoes WHERE expositor_id IN (
    SELECT id FROM expositores WHERE nome_fantasia IN ('TechSol', 'InovaDig', 'StartupTech', 'ConsEmp', 'WebSol')
);

-- Remover oportunidades relacionadas às empresas fictícias
DELETE FROM oportunidades WHERE expositor_id IN (
    SELECT id FROM expositores WHERE nome_fantasia IN ('TechSol', 'InovaDig', 'StartupTech', 'ConsEmp', 'WebSol')
);

-- Remover as empresas fictícias
DELETE FROM expositores WHERE nome_fantasia IN ('TechSol', 'InovaDig', 'StartupTech', 'ConsEmp', 'WebSol');

-- Remover usuários fictícios (exceto o administrador)
DELETE FROM usuarios WHERE email IN ('joao@crmshot.com', 'maria@crmshot.com', 'pedro@crmshot.com');

-- Verificar se ainda existem dados fictícios
SELECT 'Expositores restantes:' as info;
SELECT id, razao_social, nome_fantasia FROM expositores;

SELECT 'Usuários restantes:' as info;
SELECT id, nome, email FROM usuarios;

SELECT 'Oportunidades restantes:' as info;
SELECT id, titulo, expositor_id FROM oportunidades;

SELECT 'Interações restantes:' as info;
SELECT id, assunto, expositor_id FROM interacoes;
