#!/bin/bash

# Script de configuração local do ambiente CRM Shot Fair Brasil
# Para sistemas sem Docker

echo "🚀 Configurando ambiente CRM Shot Fair Brasil (modo local)..."

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

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não está instalado. Por favor, instale o Maven 3.6 ou superior."
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
DB_USERNAME=postgres
DB_PASSWORD=postgres

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
echo "📋 PRÓXIMOS PASSOS:"
echo ""
echo "1. Configure o banco de dados PostgreSQL:"
echo "   - Acesse o pgAdmin ou terminal do PostgreSQL"
echo "   - Crie o banco 'crmshot_db'"
echo "   - Execute os scripts em database/init.sql e database/sample_data.sql"
echo ""
echo "2. Inicie o backend:"
echo "   cd backend && mvn spring-boot:run"
echo ""
echo "3. Inicie o frontend (em outro terminal):"
echo "   cd frontend && npm start"
echo ""
echo "4. Acesse:"
echo "   - Frontend: http://localhost:3000"
echo "   - Backend API: http://localhost:8080/api"
echo ""
echo "🔑 Usuários de teste:"
echo "   - admin@crmshot.com / admin123"
echo "   - joao@crmshot.com / admin123"
echo "   - maria@crmshot.com / admin123"
