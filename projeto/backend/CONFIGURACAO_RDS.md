# Configuração do RDS no Elastic Beanstalk

## ✅ Solução Automática (Recomendado)

**O código agora descobre o RDS automaticamente!** 🎉

A aplicação tenta descobrir o banco de dados RDS PostgreSQL automaticamente usando o AWS SDK. Não é necessário configurar variáveis de ambiente manualmente.

### Como Funciona

1. **Primeiro**: Verifica variáveis de ambiente do Elastic Beanstalk (`RDS_HOSTNAME`, `RDS_PASSWORD`, etc.)
2. **Segundo**: Verifica variáveis de ambiente manuais (`AWS_RDS_HOST`, `AWS_RDS_PASSWORD`, etc.)
3. **Terceiro**: Descobre automaticamente via AWS SDK:
   - Lista todas as instâncias RDS PostgreSQL na região
   - Seleciona a primeira instância disponível
   - Tenta obter a senha do AWS Secrets Manager
   - Conecta automaticamente

### Permissões IAM Necessárias

O perfil IAM do Elastic Beanstalk precisa ter as seguintes permissões:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "rds:DescribeDBInstances",
        "rds:DescribeDBClusters"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": "arn:aws:secretsmanager:*:*:secret:rds-db-credentials/*"
    }
  ]
}
```

**Como configurar:**
1. Acesse o console do Elastic Beanstalk
2. Vá em **Configuration** > **Security** > **Instance profile and roles**
3. Edite o **Instance profile** e adicione a política acima
4. Ou crie uma nova role com essas permissões e atribua ao ambiente

### Configuração da Senha

A senha pode ser obtida de três formas (em ordem de prioridade):

1. **Variável de ambiente `RDS_PASSWORD`** (Elastic Beanstalk automático)
2. **Variável de ambiente `AWS_RDS_PASSWORD`** (manual)
3. **AWS Secrets Manager**:
   - Nome padrão: `rds-db-credentials/{instance-id}`
   - Ou configure `AWS_RDS_SECRET_NAME` com o nome do secret

---

## Configuração Manual (Alternativa)

Se preferir configurar manualmente ou se a descoberta automática não funcionar:

### Opção 1: Conectar RDS diretamente ao Elastic Beanstalk (Recomendado)

Quando você conecta um banco RDS diretamente ao ambiente do Elastic Beanstalk, as seguintes variáveis são configuradas automaticamente:

- `RDS_HOSTNAME` - Hostname do banco de dados
- `RDS_PORT` - Porta do banco (padrão: 5432)
- `RDS_DB_NAME` - Nome do banco (padrão: ebdb)
- `RDS_USERNAME` - Usuário do banco
- `RDS_PASSWORD` - Senha do banco

**Como fazer:**
1. Acesse o console do AWS Elastic Beanstalk
2. Selecione seu ambiente
3. Vá em **Configuration** > **Database**
4. Clique em **Modify**
5. Selecione ou crie uma instância RDS
6. Configure usuário e senha
7. Clique em **Apply**

### Opção 2: Configurar Variáveis de Ambiente Manualmente

Se você já tem um banco RDS criado separadamente, configure as variáveis manualmente:

**Variáveis necessárias:**
- `AWS_RDS_HOST` - Hostname do banco (ex: `crmshot-db.xxxxx.us-east-2.rds.amazonaws.com`)
- `AWS_RDS_PORT` - Porta (padrão: 5432)
- `AWS_RDS_DATABASE` - Nome do banco (ex: `crmshot`)
- `AWS_RDS_USERNAME` - Usuário (ex: `postgres`)
- `AWS_RDS_PASSWORD` - Senha do banco

**Como fazer via Console AWS:**
1. Acesse o console do AWS Elastic Beanstalk
2. Selecione seu ambiente
3. Vá em **Configuration** > **Software** > **Environment properties**
4. Adicione cada variável clicando em **Edit**
5. Clique em **Apply**

**Como fazer via AWS CLI:**
```bash
aws elasticbeanstalk update-environment \
  --environment-name SEU_ENVIRONMENT_NAME \
  --option-settings \
    Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_HOST,Value=seu-host.rds.amazonaws.com \
    Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_PORT,Value=5432 \
    Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_DATABASE,Value=crmshot \
    Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_USERNAME,Value=postgres \
    Namespace=aws:elasticbeanstalk:application:environment,OptionName=AWS_RDS_PASSWORD,Value=SUA_SENHA \
  --region us-east-2
```

## Verificação

Após configurar, a aplicação deve iniciar corretamente. Verifique os logs:

```bash
# Via AWS Console
# Vá em Monitoring > Logs > Request Logs > Last 100 Lines

# Via AWS CLI
aws elasticbeanstalk request-environment-info \
  --environment-name SEU_ENVIRONMENT_NAME \
  --info-type tail \
  --region us-east-2
```

Os logs devem mostrar:
```
INICIANDO CONFIGURAÇÃO DO DATASOURCE
RDS_HOSTNAME encontrado: xxx.rds.amazonaws.com
OU
Usando credenciais RDS configuradas via variáveis de ambiente/propriedades
Host: xxx.rds.amazonaws.com
Port: 5432
Database: crmshot
Username: postgres
JDBC URL: jdbc:postgresql://xxx.rds.amazonaws.com:5432/crmshot
```

## Troubleshooting

Se ainda houver erro 502:

1. **Verifique se o RDS está acessível:**
   - O Security Group do RDS deve permitir conexões do Security Group do Elastic Beanstalk
   - Verifique as regras de entrada no Security Group do RDS

2. **Verifique os logs da aplicação:**
   - Procure por mensagens de erro relacionadas a conexão com banco
   - Verifique se as variáveis estão sendo lidas corretamente

3. **Reinicie o ambiente:**
   ```bash
   aws elasticbeanstalk restart-app-server \
     --environment-name SEU_ENVIRONMENT_NAME \
     --region us-east-2
   ```

