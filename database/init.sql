34367746


-- Script de inicialização do banco de dados CRM Shot Fair Brasil
-- PostgreSQL

-- Criar banco de dados
CREATE DATABASE crmshot_db;

-- Conectar ao banco
\c crmshot_db;

-- Criar usuário
CREATE USER crmshot_user WITH PASSWORD 'crmshot_password';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE crmshot_db TO crmshot_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO crmshot_user;

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Comentários sobre o banco
COMMENT ON DATABASE crmshot_db IS 'Banco de dados do sistema CRM Shot Fair Brasil';
