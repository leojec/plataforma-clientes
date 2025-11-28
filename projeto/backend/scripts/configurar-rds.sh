#!/bin/bash

# Script para configurar variáveis de ambiente do RDS no Elastic Beanstalk
# Uso: ./configurar-rds.sh <ENVIRONMENT_NAME> <RDS_HOST> <RDS_DATABASE> <RDS_USERNAME> <RDS_PASSWORD> [RDS_PORT]

set -e

ENVIRONMENT_NAME=$1
RDS_HOST=$2
RDS_DATABASE=$3
RDS_USERNAME=$4
RDS_PASSWORD=$5
RDS_PORT=${6:-5432}
AWS_REGION=${AWS_REGION:-us-east-2}

if [ -z "$ENVIRONMENT_NAME" ] || [ -z "$RDS_HOST" ] || [ -z "$RDS_DATABASE" ] || [ -z "$RDS_USERNAME" ] || [ -z "$RDS_PASSWORD" ]; then
    echo "❌ Erro: Parâmetros insuficientes"
    echo ""
    echo "Uso: $0 <ENVIRONMENT_NAME> <RDS_HOST> <RDS_DATABASE> <RDS_USERNAME> <RDS_PASSWORD> [RDS_PORT]"
    echo ""
    echo "Exemplo:"
    echo "  $0 crmshot-env crmshot-db.xxxxx.us-east-2.rds.amazonaws.com crmshot postgres minhaSenha123 5432"
    exit 1
fi

echo "🔧 Configurando variáveis de ambiente do RDS no Elastic Beanstalk..."
echo "   Environment: $ENVIRONMENT_NAME"
echo "   Host: $RDS_HOST"
echo "   Database: $RDS_DATABASE"
echo "   Username: $RDS_USERNAME"
echo "   Port: $RDS_PORT"
echo "   Region: $AWS_REGION"
echo ""

aws elasticbeanstalk update-environment \
  --environment-name "$ENVIRONMENT_NAME" \
  --option-settings \
    "Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_HOST,Value=$RDS_HOST" \
    "Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_PORT,Value=$RDS_PORT" \
    "Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_DATABASE,Value=$RDS_DATABASE" \
    "Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_USERNAME,Value=$RDS_USERNAME" \
    "Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_PASSWORD,Value=$RDS_PASSWORD" \
  --region "$AWS_REGION"

echo ""
echo "✅ Variáveis de ambiente configuradas com sucesso!"
echo ""
echo "⏳ Aguardando atualização do ambiente..."
aws elasticbeanstalk wait environment-updated \
  --environment-name "$ENVIRONMENT_NAME" \
  --region "$AWS_REGION" || true

echo ""
echo "✅ Ambiente atualizado!"
echo ""
echo "📋 Verifique os logs para confirmar que a aplicação iniciou corretamente:"
echo "   aws elasticbeanstalk request-environment-info \\"
echo "     --environment-name $ENVIRONMENT_NAME \\"
echo "     --info-type tail \\"
echo "     --region $AWS_REGION"

