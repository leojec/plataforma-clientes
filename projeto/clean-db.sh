#!/bin/bash

echo "🧹 Limpando banco de dados..."

# Parar backend
pkill -f "spring-boot:run" 2>/dev/null
sleep 2

# Limpar banco de dados (usando H2 em memória para simplificar)
echo "🗑️ Removendo dados fictícios..."

# Reiniciar backend
echo "🔄 Reiniciando backend..."
cd backend
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Aguardar backend inicializar
echo "⏳ Aguardando backend inicializar..."
sleep 30

# Verificar se backend está rodando
if curl -s http://localhost:8080/api/test/hello > /dev/null; then
    echo "✅ Backend rodando com dados limpos!"
    echo ""
    echo "📊 Dados atuais:"
    echo "   - Usuários: $(curl -s http://localhost:8080/api/relatorios/resumo-executivo | grep -o '"totalExpositores":[0-9]*' | cut -d: -f2)"
    echo "   - Oportunidades: $(curl -s http://localhost:8080/api/relatorios/resumo-executivo | grep -o '"totalOportunidades":[0-9]*' | cut -d: -f2)"
    echo ""
    echo "🎯 Sistema pronto para uso com dados reais!"
else
    echo "❌ Erro ao iniciar backend"
    exit 1
fi
