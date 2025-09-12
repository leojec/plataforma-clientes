#!/bin/bash

echo "🚀 Iniciando CRM Shot Fair Brasil (Versão Simplificada)..."

# Parar processos existentes
echo "🛑 Parando processos existentes..."
pkill -f "spring-boot:run" 2>/dev/null
pkill -f "react-scripts" 2>/dev/null
sleep 2

# Iniciar backend
echo "☕ Iniciando backend..."
cd backend
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Aguardar backend
echo "⏳ Aguardando backend inicializar..."
sleep 30

# Testar backend
echo "🔍 Testando backend..."
if curl -s http://localhost:8080/api/test/hello > /dev/null; then
    echo "✅ Backend funcionando!"
    
    # Testar login
    echo "🔑 Testando login..."
    LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
        -H "Content-Type: application/json" \
        -d '{"email":"admin@crmshot.com","senha":"admin123"}')
    
    if [[ $LOGIN_RESPONSE == *"token"* ]]; then
        echo "✅ Login funcionando!"
        echo "🎉 Sistema pronto!"
        echo ""
        echo "📱 Acesse: http://localhost:3000"
        echo "🔧 Backend API: http://localhost:8080/api"
        echo ""
        echo "🔑 Usuários de teste:"
        echo "   - admin@crmshot.com / admin123"
        echo "   - joao@crmshot.com / admin123"
        echo "   - maria@crmshot.com / admin123"
    else
        echo "❌ Erro no login: $LOGIN_RESPONSE"
        echo "📋 Verifique backend.log para mais detalhes"
    fi
else
    echo "❌ Backend não está respondendo"
    echo "📋 Verifique backend.log para mais detalhes"
    exit 1
fi

# Iniciar frontend
echo "⚛️ Iniciando frontend..."
cd frontend
npm start &
FRONTEND_PID=$!
cd ..

echo ""
echo "🎯 Sistema iniciado! Para parar, pressione Ctrl+C"
echo "📱 Frontend: http://localhost:3000"
echo "🔧 Backend: http://localhost:8080/api"

# Manter script rodando
wait
