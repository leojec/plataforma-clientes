#!/bin/bash

echo "🚀 Iniciando CRM Shot Fair Brasil..."

# Verificar se o PostgreSQL está rodando
if ! pg_isready -q; then
    echo "❌ PostgreSQL não está rodando. Iniciando..."
    brew services start postgresql@15 || brew services start postgresql
    sleep 5
fi

# Criar banco se não existir
echo "🗄️ Configurando banco de dados..."
psql -U postgres -c "CREATE DATABASE crmshot_db;" 2>/dev/null || echo "Banco já existe"

# Iniciar backend em background
echo "☕ Iniciando backend..."
cd backend
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Aguardar backend inicializar
echo "⏳ Aguardando backend inicializar..."
sleep 30

# Verificar se backend está rodando
if curl -s http://localhost:8080/api/auth/login > /dev/null; then
    echo "✅ Backend rodando em http://localhost:8080"
else
    echo "❌ Erro ao iniciar backend. Verifique backend.log"
    exit 1
fi

# Iniciar frontend
echo "⚛️ Iniciando frontend..."
cd frontend
npm start &
FRONTEND_PID=$!
cd ..

echo ""
echo "🎉 Sistema iniciado com sucesso!"
echo ""
echo "📱 Acesse: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080/api"
echo ""
echo "🔑 Usuários de teste:"
echo "   - admin@crmshot.com / admin123"
echo "   - joao@crmshot.com / admin123"
echo "   - maria@crmshot.com / admin123"
echo ""
echo "Para parar o sistema, pressione Ctrl+C"

# Manter script rodando
wait
