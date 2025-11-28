# 🎯 CRM Shot Fair Brasil - Sistema de Gestão de Relacionamento com Clientes

Sistema completo de CRM para gerenciar **expositores**, **oportunidades de vendas**, **interações** e **atividades** em eventos, com pipeline visual, dashboard analítico e assistente inteligente.

## 🚀 **Status do Projeto**

✅ **Backend**: 100% implementado (Spring Boot + JPA + PostgreSQL)  
✅ **Frontend**: 100% implementado (React + Tailwind CSS)  
✅ **Banco de Dados**: Estrutura completa com entidades relacionadas  
✅ **API REST**: Endpoints CRUD para todas as funcionalidades  
✅ **Interface**: Páginas principais e componentes responsivos  
✅ **Autenticação**: JWT implementado com segurança  
✅ **Testes**: Cobertura de testes unitários e de integração  

## ⚡ **Início Rápido - Um Comando Para Tudo**

```bash
./start.sh
```

✨ Este comando inicia **tudo automaticamente**: PostgreSQL + Backend + Frontend!

Para parar:
```bash
# Pressione Ctrl+C no terminal onde o script está rodando
```

📖 **Mais detalhes**: Veja a seção [Como Executar](#-como-executar) abaixo.

---

## 🎯 **Funcionalidades Implementadas**

### **Gestão de Expositores**
- **CRUD completo** de expositores (clientes)
- **Validação de CNPJ** em tempo real
- **Busca e filtros** avançados
- **Histórico completo** de interações

### **Pipeline de Vendas (Kanban)**
- **Visualização em colunas** (Lead, Em Andamento, Em Negociação, Stand Fechado)
- **Drag and Drop** para mover oportunidades entre estágios
- **Detalhes completos** de cada lead
- **Atualização em tempo real** do status

### **Dashboard Analítico**
- **Estatísticas gerais**: novos clientes, atividades, vendas
- **Gráficos interativos** de performance
- **Métricas de vendas**: metros quadrados, propostas, ganhos
- **Visualização por período** e por vendedor

### **Agenda de Atividades**
- **Calendário de interações** (reuniões, ligações, e-mails)
- **Lembretes e follow-ups** automáticos
- **Histórico completo** de atividades por expositor
- **Marcação de conclusão** de tarefas

### **Relatórios Gerenciais**
- **Relatórios executivos** consolidados
- **Análise de performance** de vendedores
- **Oportunidades por status** e período
- **Exportação de dados** (preparado para PDF/Excel)

### **Assistente CRM Inteligente**
- **Chatbot interativo** para consultas rápidas
- **Perguntas sobre**: próximas reuniões, quantidade de leads, atividades do dia
- **Respostas em tempo real** baseadas nos dados do sistema
- **Interface amigável** e intuitiva

### **Autenticação e Segurança**
- **Login seguro** com JWT
- **Perfis de usuário**: Administrador, Gerente, Vendedor
- **Proteção de rotas** e endpoints
- **Criptografia de senhas** com BCrypt

## 🛠️ **Stack Tecnológica**

### **Backend**
- **Framework**: Spring Boot 3.2.0
- **Linguagem**: Java 17
- **ORM**: Spring Data JPA / Hibernate
- **Banco de Dados**: PostgreSQL
- **Build**: Maven
- **Testes**: JUnit 5 + Mockito
- **Cobertura**: JaCoCo

### **Frontend**
- **Framework**: React 18.2
- **Linguagem**: JavaScript (ES6+)
- **Estilização**: Tailwind CSS
- **Roteamento**: React Router DOM
- **Requisições**: Axios
- **Estado**: React Query
- **UI Components**: Lucide React Icons
- **Drag & Drop**: @dnd-kit
- **Gráficos**: CanvasJS + Recharts

### **Infraestrutura**
- **Banco de Dados**: PostgreSQL (local ou AWS RDS)
- **Deploy Backend**: AWS Elastic Beanstalk
- **Deploy Frontend**: Preparado para Vercel/Netlify
- **CI/CD**: Configurável (GitHub Actions)

## 🏗️ **Arquitetura**

```
Frontend (React) ⇄ API REST (Spring Boot) ⇄ PostgreSQL
                      ↓
                  AWS RDS (Produção)
```

### **Módulos Principais**
- **Auth**: Autenticação e autorização
- **Expositores**: Gestão de clientes/expositores
- **Oportunidades**: Pipeline de vendas
- **Interações**: Histórico de atividades
- **Agenda**: Calendário e lembretes
- **Dashboard**: Analytics e métricas
- **Relatórios**: Geração de relatórios
- **Chat**: Assistente CRM inteligente

### **Entidades Principais**
- `Usuario`: Usuários do sistema (vendedores, gerentes, admin)
- `Expositor`: Clientes/expositores cadastrados
- `Oportunidade`: Oportunidades de venda
- `Interacao`: Histórico de interações (reuniões, ligações, e-mails)

## ▶️ **Como Executar**

### **1. Pré-requisitos**
- **Java**: JDK 17 ou superior
- **Node.js**: 18+ e npm
- **PostgreSQL**: 12+ (ou Docker)
- **Maven**: 3.8+ (geralmente incluído com IDEs)

### **2. Clone o Repositório**
```bash
git clone <seu-repositorio>
cd TCC/projeto
```

### **3. Configuração do Banco de Dados**

#### **Opção A: PostgreSQL Local**
```bash
# Iniciar PostgreSQL (macOS com Homebrew)
brew services start postgresql@15

# Criar banco de dados
psql -U postgres -c "CREATE DATABASE crmshot;"
```

#### **Opção B: Docker**
```bash
docker run --name crmshot-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=crmshot \
  -p 5432:5432 \
  -d postgres:15
```

### **4. Backend**

```bash
cd backend

# Instalar dependências (Maven baixa automaticamente)
# Não é necessário npm install para backend

# Configurar variáveis de ambiente (opcional)
# As credenciais padrão estão no código (RdsConfig.java)
# Para produção, configure via AWS Elastic Beanstalk

# Executar aplicação
mvn spring-boot:run

# Ou compilar e executar
mvn clean package
java -jar target/crmshot-backend-0.0.1-SNAPSHOT.jar
```

**Backend estará disponível em**: `http://localhost:8080`

### **5. Frontend**

```bash
cd frontend

# Instalar dependências
npm install

# Configurar variáveis de ambiente (opcional)
# Criar arquivo .env se necessário:
# REACT_APP_BACKEND_URL=http://localhost:8080

# Rodar em desenvolvimento
npm start
```

**Frontend estará disponível em**: `http://localhost:3000`

### **6. Script Automatizado (Recomendado)**

```bash
# Dar permissão de execução (primeira vez)
chmod +x start.sh

# Executar script
./start.sh
```

Este script:
- ✅ Verifica e inicia PostgreSQL
- ✅ Cria o banco de dados se não existir
- ✅ Inicia o backend em background
- ✅ Aguarda backend inicializar
- ✅ Inicia o frontend
- ✅ Exibe URLs de acesso e credenciais

## 🌐 **URLs de Acesso**

### **Desenvolvimento Local**
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/health

### **Produção (AWS)**
- **Frontend**: (Configurar no Vercel/Netlify)
- **Backend API**: (URL do Elastic Beanstalk)
- **Banco de Dados**: AWS RDS PostgreSQL

## 🔑 **Credenciais de Teste**

O sistema vem com usuários pré-configurados:

| Email | Senha | Perfil |
|-------|-------|--------|
| admin@crmshot.com | admin123 | Administrador |
| joao@crmshot.com | admin123 | Vendedor |
| maria@crmshot.com | admin123 | Vendedor |

## 📊 **Estrutura do Banco de Dados**

### **Tabelas Principais**
- `usuarios` - Usuários do sistema (vendedores, gerentes, admin)
- `expositores` - Clientes/expositores cadastrados
- `oportunidades` - Oportunidades de venda
- `interacoes` - Histórico de interações (reuniões, ligações, e-mails)

### **Relacionamentos**
- `Expositor` → `Oportunidade` (1:N)
- `Expositor` → `Interacao` (1:N)
- `Usuario` → `Oportunidade` (1:N) - Responsável pela venda
- `Usuario` → `Interacao` (1:N) - Criador da interação

### **Regras de Negócio**
- **Status do Expositor**: POTENCIAL, ATIVO, INATIVO, BLOQUEADO
- **Status da Oportunidade**: Configurável no pipeline
- **Tipos de Interação**: REUNIAO, LIGACAO, EMAIL, VISITA
- **Cálculo de Totais**: Automático baseado em propostas e vendas

## 🔧 **Endpoints da API**

### **Autenticação**
- `POST /api/auth/login` - Login e obtenção de token JWT
- `POST /api/auth/register` - Registro de novo usuário (admin)

### **Expositores**
- `GET /api/expositores` - Listar todos os expositores
- `GET /api/expositores/:id` - Buscar expositor por ID
- `POST /api/expositores` - Criar novo expositor
- `PUT /api/expositores/:id` - Atualizar expositor
- `DELETE /api/expositores/:id` - Remover expositor
- `PUT /api/expositores/:id/status` - Atualizar status do expositor

### **Oportunidades**
- `GET /api/oportunidades` - Listar todas as oportunidades
- `GET /api/oportunidades/:id` - Buscar oportunidade por ID
- `POST /api/oportunidades` - Criar nova oportunidade
- `PUT /api/oportunidades/:id` - Atualizar oportunidade
- `DELETE /api/oportunidades/:id` - Remover oportunidade

### **Agenda/Interações**
- `GET /api/agenda/atividades` - Listar todas as atividades
- `GET /api/agenda/atividades/lead/:leadId` - Atividades de um lead
- `POST /api/agenda/atividades` - Criar nova atividade
- `PUT /api/agenda/atividades/:id` - Atualizar atividade
- `PUT /api/agenda/atividades/:id/concluir` - Marcar como concluída
- `DELETE /api/agenda/atividades/:id` - Remover atividade

### **Dashboard**
- `GET /api/dashboard/stats` - Estatísticas gerais
- `GET /api/dashboard/atividades-grafico` - Dados para gráfico de atividades

### **Relatórios**
- `GET /api/relatorios/resumo-executivo` - Resumo executivo
- `GET /api/relatorios/oportunidades-por-status` - Oportunidades por status
- `GET /api/relatorios/vendas-por-mes` - Vendas por mês
- `GET /api/relatorios/performance-vendedores` - Performance de vendedores

### **Chat/Assistente**
- `POST /api/chat/perguntar` - Fazer pergunta ao assistente CRM

### **Utilitários**
- `GET /api/cnpj/:cnpj` - Consultar CNPJ na Receita Federal
- `GET /api/health` - Health check da API

## 🎨 **Interface do Usuário**

### **Páginas Principais**
- **Login**: Autenticação de usuários
- **Dashboard**: Visão geral com estatísticas e gráficos
- **Kanban Board**: Pipeline visual de vendas (drag & drop)
- **Expositores**: CRUD completo de clientes
- **Agenda**: Calendário de atividades e interações
- **Relatórios**: Relatórios gerenciais e análises
- **Configurações**: Configurações do sistema

### **Componentes Principais**
- **Layout**: Estrutura base com sidebar e navegação
- **ChatBot**: Assistente CRM inteligente (flutuante)
- **StatusModal**: Modal para alterar status de leads
- **ActivityModal**: Modal para criar/editar atividades
- **AddClientModal**: Modal para adicionar novos expositores

## 🧪 **Testes**

### **Backend**
```bash
cd backend

# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn clean test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

### **Frontend**
```bash
cd frontend

# Executar testes
npm test

# Executar testes com cobertura
npm test -- --coverage
```

### **Cobertura de Testes**
- **Backend**: Testes unitários e de integração para controllers, services, repositories
- **Frontend**: Testes de componentes e páginas principais
- **Relatórios**: JaCoCo (backend) e Jest Coverage (frontend)

## 🚀 **Deploy**

### **Backend (AWS Elastic Beanstalk)**
```bash
cd backend

# Build do projeto
mvn clean package

# Deploy via EB CLI (se configurado)
eb deploy

# Ou fazer upload manual do .jar para Elastic Beanstalk
```

**Variáveis de Ambiente no Elastic Beanstalk:**
- `RDS_HOSTNAME`: Host do RDS (automático se RDS conectado)
- `RDS_DB_NAME`: Nome do banco
- `RDS_USERNAME`: Usuário do banco
- `RDS_PASSWORD`: Senha do banco
- `RDS_PORT`: Porta do banco (5432)
- `JWT_SECRET`: Chave secreta para JWT

### **Frontend (Vercel/Netlify)**
```bash
cd frontend

# Build de produção
npm run build

# Deploy (exemplo Vercel)
vercel --prod
```

**Variáveis de Ambiente:**
- `REACT_APP_BACKEND_URL`: URL do backend (Elastic Beanstalk)
- `REACT_APP_API_URL`: URL da API (opcional)

### **Banco de Dados (AWS RDS)**
- **Tipo**: PostgreSQL
- **Configuração**: Automática via Elastic Beanstalk ou manual
- **Backup**: Configurar backups automáticos no RDS
- **Segurança**: Security Groups configurados

## 📚 **Documentação Adicional**

- **Configuração RDS**: `backend/CONFIGURACAO_RDS.md`
- **Permissões IAM**: `backend/PERMISSOES_IAM.md`
- **Relatório de Cobertura**: `backend/COMO-GERAR-RELATORIO-COBERTURA.md`

## ✅ **Próximos Passos**

1. **Melhorias de Performance**
   - Cache Redis para consultas frequentes
   - Otimização de queries do banco
   - Lazy loading no frontend

2. **Funcionalidades Adicionais**
   - Exportação de relatórios em PDF/Excel
   - Notificações push para lembretes
   - Integração com calendário (Google Calendar, Outlook)
   - Dashboard customizável por usuário

3. **Segurança Avançada**
   - Autenticação de dois fatores (2FA)
   - Rate limiting nas APIs
   - Auditoria completa de ações
   - Logs de segurança

4. **Integrações**
   - Integração com ERPs
   - Integração com sistemas de pagamento
   - API pública para parceiros
   - Webhooks para eventos

5. **Mobile**
   - Aplicativo mobile (React Native)
   - Notificações mobile
   - Acesso offline

## 🤝 **Contribuição**

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 🔒 **Conformidade e Segurança**

### **LGPD (Lei Geral de Proteção de Dados)**
✅ Política de Privacidade (a implementar)  
✅ Termos de Uso (a implementar)  
✅ Consentimento do usuário  
✅ Direitos do titular (acesso, correção, exclusão)  
✅ Segurança de dados (criptografia, autenticação JWT)

### **Segurança (OWASP Top 10)**
✅ Proteção contra SQL Injection (JPA/Hibernate)  
✅ Autenticação segura (JWT + BCrypt)  
✅ Validação de inputs (Bean Validation)  
✅ Headers de segurança (Spring Security)  
✅ CORS configurado  
✅ Senhas criptografadas

### **Boas Práticas**
✅ Código limpo e sem comentários desnecessários  
✅ Testes unitários e de integração  
✅ Documentação de API  
✅ Tratamento de erros adequado  
✅ Logs estruturados

## 📄 **Licenciamento**

### **Licença do Projeto**
**MIT License** - Código aberto e uso livre para fins acadêmicos e comerciais.

### **Dependências de Terceiros**
Todas as dependências utilizam licenças permissivas (MIT, Apache 2.0, ISC).  
Consulte os arquivos `pom.xml` (backend) e `package.json` (frontend) para lista completa.

---

## 📝 **Sobre o Projeto**

**CRM Shot Fair Brasil** é um sistema desenvolvido como Trabalho de Conclusão de Curso (TCC) para o curso de Engenharia de Software do Centro Universitário Católica de Santa Catarina - Joinville.

**Autor**: Leonardo Luis da Rocha  
**Orientador**: [Nome do Orientador]  
**Ano**: 2025

---

**🎉 Projeto pronto para uso e em constante evolução!**  
Execute os comandos acima e comece a gerenciar seus expositores de forma eficiente.

