# Permissões IAM Necessárias para Descoberta Automática do RDS

## Política IAM Completa

Para que a aplicação descubra o RDS automaticamente, o perfil IAM do Elastic Beanstalk precisa das seguintes permissões:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowRDSDiscovery",
      "Effect": "Allow",
      "Action": [
        "rds:DescribeDBInstances",
        "rds:DescribeDBClusters",
        "rds:ListTagsForResource"
      ],
      "Resource": "*"
    },
    {
      "Sid": "AllowSecretsManagerAccess",
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue",
        "secretsmanager:DescribeSecret"
      ],
      "Resource": [
        "arn:aws:secretsmanager:*:*:secret:rds-db-credentials/*",
        "arn:aws:secretsmanager:*:*:secret:*rds*"
      ]
    }
  ]
}
```

## Como Configurar no Elastic Beanstalk

### Opção 1: Via Console AWS

1. Acesse o **AWS IAM Console**
2. Vá em **Roles** e encontre a role usada pelo Elastic Beanstalk (geralmente `aws-elasticbeanstalk-ec2-role`)
3. Clique em **Add permissions** > **Create inline policy**
4. Cole a política JSON acima
5. Dê um nome (ex: `RDSAutoDiscoveryPolicy`)
6. Clique em **Create policy**

### Opção 2: Via AWS CLI

```bash
# Obter o nome da role do ambiente
ROLE_NAME=$(aws elasticbeanstalk describe-environment-resources \
  --environment-name SEU_ENVIRONMENT_NAME \
  --region us-east-2 \
  --query 'EnvironmentResources.IamInstanceProfile' \
  --output text | cut -d'/' -f2)

# Criar política inline
aws iam put-role-policy \
  --role-name $ROLE_NAME \
  --policy-name RDSAutoDiscoveryPolicy \
  --policy-document file://rds-policy.json \
  --region us-east-2
```

Onde `rds-policy.json` contém a política JSON acima.

### Opção 3: Via Terraform/CloudFormation

Se você usa IaC, adicione a política à role do Elastic Beanstalk:

```hcl
# Terraform
resource "aws_iam_role_policy" "rds_discovery" {
  name = "RDSAutoDiscoveryPolicy"
  role = aws_iam_role.elastic_beanstalk.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "rds:DescribeDBInstances",
          "rds:DescribeDBClusters"
        ]
        Resource = "*"
      },
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue"
        ]
        Resource = "arn:aws:secretsmanager:*:*:secret:rds-db-credentials/*"
      }
    ]
  })
}
```

## Verificação

Após configurar as permissões, verifique se funcionou:

1. Faça deploy da aplicação
2. Verifique os logs do Elastic Beanstalk
3. Procure por mensagens como:
   - `"Tentando descobrir RDS automaticamente via AWS SDK..."`
   - `"Encontradas X instância(s) RDS na região"`
   - `"RDS descoberto automaticamente via AWS SDK"`

## Troubleshooting

### Erro: "Access Denied" ao tentar descobrir RDS

**Solução**: Verifique se a role IAM tem as permissões corretas e se está anexada ao ambiente do Elastic Beanstalk.

### Erro: "Nenhuma instância PostgreSQL disponível encontrada"

**Solução**: 
- Verifique se existe uma instância RDS PostgreSQL na mesma região
- Verifique se a instância está com status "available"
- Verifique se o engine é "postgres" ou "postgresql"

### Erro: "Senha não encontrada no Secrets Manager"

**Solução**:
- Configure a variável `AWS_RDS_PASSWORD` manualmente
- Ou crie um secret no Secrets Manager com o nome `rds-db-credentials/{instance-id}`
- Ou configure `AWS_RDS_SECRET_NAME` com o nome do secret

