#!/bin/bash

echo "🗄️ Configurando banco de dados PostgreSQL..."

# Tentar conectar e criar o banco
echo "Criando banco de dados 'crmshot_db'..."

# Primeiro, vamos tentar conectar sem senha
psql -U postgres -c "CREATE DATABASE crmshot_db;" 2>/dev/null && echo "✅ Banco criado com sucesso!" || {
    echo "❌ Erro ao criar banco. Vamos tentar outras opções..."
    
    # Tentar com usuário atual
    createdb crmshot_db 2>/dev/null && echo "✅ Banco criado com usuário atual!" || {
        echo "❌ Não foi possível criar o banco automaticamente."
        echo ""
        echo "📋 INSTRUÇÕES MANUAIS:"
        echo "1. Abra o pgAdmin ou terminal do PostgreSQL"
        echo "2. Crie um banco chamado 'crmshot_db'"
        echo "3. Execute os scripts:"
        echo "   - database/init.sql"
        echo "   - database/sample_data.sql"
        echo ""
        echo "Ou execute manualmente:"
        echo "psql -U postgres -c \"CREATE DATABASE crmshot_db;\""
        echo "psql -U postgres -d crmshot_db -f database/init.sql"
        echo "psql -U postgres -d crmshot_db -f database/sample_data.sql"
    }
}

# Se o banco foi criado, executar os scripts
if psql -U postgres -d crmshot_db -c "SELECT 1;" 2>/dev/null; then
    echo "📊 Executando scripts de inicialização..."
    psql -U postgres -d crmshot_db -f database/init.sql
    psql -U postgres -d crmshot_db -f database/sample_data.sql
    echo "✅ Scripts executados com sucesso!"
elif createdb crmshot_db 2>/dev/null; then
    echo "📊 Executando scripts de inicialização..."
    psql -d crmshot_db -f database/init.sql
    psql -d crmshot_db -f database/sample_data.sql
    echo "✅ Scripts executados com sucesso!"
fi

echo ""
echo "🎉 Configuração do banco concluída!"
echo "Agora você pode iniciar o backend e frontend."
