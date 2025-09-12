#!/bin/bash

# Script de configuração do ambiente CRM Shot Fair Brasil
# Execute este script para configurar o ambiente de desenvolvimento

echo "🚀 Configurando ambiente CRM Shot Fair Brasil..."

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado. Por favor, instale o Docker primeiro."
    exit 1
fi

# Verificar se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado. Por favor, instale o Docker Compose primeiro."
    exit 1
fi

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não está instalado. Por favor, instale o Java 17 ou superior."
    exit 1
fi

# Verificar se Node.js está instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js não está instalado. Por favor, instale o Node.js 16 ou superior."
    exit 1
fi

echo "✅ Dependências verificadas com sucesso!"

# Criar arquivo .env para o backend
echo "📝 Criando arquivo de configuração do backend..."
cat > backend/.env << EOF
# Configurações do Banco de Dados
DB_HOST=localhost
DB_PORT=5432
DB_NAME=crmshot_db
DB_USERNAME=crmshot_user
DB_PASSWORD=crmshot_password

# Configurações JWT
JWT_SECRET=crmshot_jwt_secret_key_2024_very_secure_key_for_production
JWT_EXPIRATION=86400000

# Configurações do Servidor
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
EOF

# Criar arquivo .env para o frontend
echo "📝 Criando arquivo de configuração do frontend..."
cat > frontend/.env << EOF
REACT_APP_API_URL=http://localhost:8080/api
EOF

echo "✅ Arquivos de configuração criados!"

# Iniciar banco de dados PostgreSQL com Docker
echo "🐘 Iniciando banco de dados PostgreSQL..."
docker run -d \
  --name crmshot-postgres \
  -e POSTGRES_DB=crmshot_db \
  -e POSTGRES_USER=crmshot_user \
  -e POSTGRES_PASSWORD=crmshot_password \
  -p 5432:5432 \
  postgres:15

# Aguardar o banco inicializar
echo "⏳ Aguardando banco de dados inicializar..."
sleep 10

# Executar scripts de inicialização do banco
echo "📊 Executando scripts de inicialização do banco..."
docker exec -i crmshot-postgres psql -U crmshot_user -d crmshot_db < database/init.sql
docker exec -i crmshot-postgres psql -U crmshot_user -d crmshot_db < database/sample_data.sql

echo "✅ Banco de dados configurado com sucesso!"

# Instalar dependências do backend
echo "☕ Instalando dependências do backend..."
cd backend
if [ -f "pom.xml" ]; then
    mvn clean install -DskipTests
    echo "✅ Dependências do backend instaladas!"
else
    echo "❌ Arquivo pom.xml não encontrado no diretório backend"
fi
cd ..

# Instalar dependências do frontend
echo "⚛️ Instalando dependências do frontend..."
cd frontend
if [ -f "package.json" ]; then
    npm install
    echo "✅ Dependências do frontend instaladas!"
else
    echo "❌ Arquivo package.json não encontrado no diretório frontend"
fi
cd ..

echo ""
echo "🎉 Configuração concluída com sucesso!"
echo ""
echo "Para iniciar o sistema:"
echo "1. Backend: cd backend && mvn spring-boot:run"
echo "2. Frontend: cd frontend && npm start"
echo ""
echo "Acesse:"
echo "- Frontend: http://localhost:3000"
echo "- Backend API: http://localhost:8080/api"
echo "- Banco de dados: localhost:5432"
echo ""
echo "Usuários de teste:"
echo "- admin@crmshot.com / admin123"
echo "- joao@crmshot.com / admin123"
echo "- maria@crmshot.com / admin123"
