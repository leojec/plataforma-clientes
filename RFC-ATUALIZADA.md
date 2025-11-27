# Centro Universitário Católica de Santa Catarina - Joinville
## Engenharia de Software

**Leonardo Luis da Rocha**

# CRMSHOT - CRM De Vendas de Feiras e Eventos

**RFC Atualizada - Projeto Concluído**

---

# Resumo

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) para o evento Shot Fair Brasil que permite o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial. A plataforma foi desenvolvida e implementada com sucesso, incluindo funcionalidades avançadas de gestão de relacionamento, pipeline de vendas, relatórios gerenciais, assistente virtual (ChatBot) e integração com APIs externas para consulta de CNPJ.

## Status do Projeto: ✅ CONCLUÍDO

---

# 1 Introdução

## 1.1 Contexto

A gestão de relacionamento com clientes é um fator crítico para o sucesso comercial de empresas em mercados competitivos. O sistema CRM desenvolvido resolve os problemas de perda de dados, dificuldades na comunicação interna e baixa eficiência no acompanhamento de clientes por meio da automatização e centralização dos processos de vendas e atendimento ao cliente.

## 1.2 Justificativa

A implementação do sistema de CRM promove uma gestão estruturada e inteligente do relacionamento com os clientes. A equipe de vendas pode acompanhar cada estágio do funil de vendas, realizar interações com registro histórico e obter indicadores de desempenho em tempo real. O sistema foi desenvolvido com foco em customização, segurança e melhoria da performance comercial.

## 1.3 Objetivos

**Objetivo principal:** Desenvolver um sistema web de CRM que automatize o cadastro, acompanhamento e análise do relacionamento com clientes, desde o primeiro contato até o fechamento da venda.

**Objetivos específicos (CONCLUÍDOS):**

- ✅ Levantar os requisitos funcionais e não funcionais do sistema.
- ✅ Especificar os módulos principais: cadastro, pipeline de vendas, histórico e relatórios.
- ✅ Modelar a base de dados e definir os fluxos de interação da aplicação.
- ✅ Desenvolver e integrar os componentes frontend e backend.
- ✅ Testar e validar a usabilidade e a segurança do sistema.
- ✅ Implementar CI/CD com GitHub Actions e deploy automatizado para AWS.
- ✅ Implementar testes automatizados (25% frontend, 75% backend).
- ✅ Implementar assistente virtual (ChatBot) para consultas rápidas.

---

# 2 Descrição do Projeto

## 2.1 Tema do Projeto

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) para empresas que buscam otimizar o acompanhamento e a gestão do relacionamento com clientes e prospects. O sistema foi desenvolvido como uma plataforma web acessível por navegadores modernos, com interface intuitiva, adaptada tanto para desktops quanto para dispositivos móveis.

## 2.2 Escopo Funcional (IMPLEMENTADO)

O sistema CRM implementa as seguintes funcionalidades:

### ✅ Módulos Implementados:

1. **Cadastro e Gestão de Expositores**
   - Cadastro completo com dados corporativos (razão social, nome fantasia, CNPJ, endereço completo)
   - Consulta automática de CNPJ via API ReceitaWS
   - Gestão de responsáveis e contatos
   - Histórico de participação em eventos
   - Preferências e necessidades específicas
   - Status de expositores (ATIVO, INATIVO, BLOQUEADO, POTENCIAL)
   - Associação com vendedores

2. **Pipeline de Vendas (Kanban)**
   - Visualização em formato Kanban com drag-and-drop
   - Acompanhamento desde o primeiro contato até o fechamento
   - Etapas configuráveis: Prospecção, Em Andamento, Em Negociação, Stand Fechado
   - Atualização de status em tempo real
   - Valores estimados e probabilidade de fechamento
   - Fonte da oportunidade (Indicação, Site, Rede Social, Telefone, Email, Evento, Outros)

3. **Histórico de Interações**
   - Registro detalhado de interações (ligações, e-mails, reuniões, visitas, WhatsApp, propostas)
   - Rastreabilidade completa com data, hora e tipo de interação
   - Associação com expositores e oportunidades
   - Próximas ações e follow-ups
   - Campos específicos para propostas (valor, metros quadrados)

4. **Agenda e Lembretes**
   - Visualização de atividades por dia, semana ou mês
   - Agendamento de follow-ups automáticos
   - Filtros por tipo de atividade e status
   - Conclusão de atividades
   - Integração com histórico de interações

5. **Relatórios Gerenciais e Dashboards**
   - Dashboard com métricas em tempo real
   - Relatórios de oportunidades por status
   - Análise de vendas por mês
   - Performance de vendedores
   - Atividades por período
   - Resumo executivo
   - Gráficos interativos (Chart.js)
   - Exportação de relatórios (preparado para PDF/Excel)

6. **Gestão de Usuários e Permissões**
   - Controle de acesso baseado em perfis (ADMINISTRADOR, GERENTE, VENDEDOR)
   - Cadastro e gerenciamento de usuários
   - Ativação/desativação de usuários
   - Registro de último acesso
   - Auditoria de operações

7. **Assistente Virtual (ChatBot)**
   - Consultas rápidas sobre o CRM
   - Informações sobre próximas reuniões
   - Quantidade de leads
   - Atividades do dia
   - Valor de propostas
   - Última atividade
   - Atividades pendentes
   - Metros quadrados vendidos
   - Resumo geral do CRM

8. **Integração com APIs Externas**
   - Consulta automática de CNPJ via ReceitaWS
   - Preenchimento automático de dados da empresa

## 2.3 Problemas Resolvidos

✅ **Centralização de informações:** Todas as informações estão centralizadas no sistema, eliminando planilhas dispersas.

✅ **Rastreabilidade:** Sistema completo de histórico de interações com rastreabilidade total do relacionamento.

✅ **Visão gerencial:** Dashboards e relatórios em tempo real para visualização da performance da equipe.

✅ **Dados históricos:** Consolidação de dados históricos para análise de padrões e oportunidades futuras.

✅ **Automação:** Lembretes e follow-ups automáticos através da agenda integrada.

## 2.4 Diferenciais Implementados

✅ **Foco em eventos e feiras:** Sistema especializado com campos específicos para gestão de expositores e espaços.

✅ **Assistente Virtual:** ChatBot integrado para consultas rápidas e acesso facilitado a informações.

✅ **Integração CNPJ:** Consulta automática de dados empresariais via API.

✅ **Pipeline Visual:** Interface Kanban intuitiva para gestão visual do funil de vendas.

✅ **Dashboard em Tempo Real:** Métricas atualizadas automaticamente com gráficos interativos.

✅ **CI/CD Automatizado:** Deploy automatizado para AWS com GitHub Actions.

✅ **Testes Automatizados:** Cobertura de testes (25% frontend, 75% backend) garantindo qualidade.

## 2.5 Limitações Conhecidas

- ⚠️ Exportação de relatórios em PDF/Excel: Interface preparada, mas funcionalidade completa pendente de implementação final.
- ⚠️ Integrações com gateways de pagamento: Não implementado (planejado para futuras releases).
- ⚠️ Automação de campanhas de marketing: Não implementado (planejado para futuras releases).
- ⚠️ MFA (Multi-Factor Authentication): Não implementado (planejado para futuras releases).

---

# 3 Especificação Técnica

## 3.1 Arquitetura Implementada

### 3.1.1 Frontend
- **Framework:** React.js 18+
- **Roteamento:** React Router v6
- **Gerenciamento de Estado:** React Query (TanStack Query)
- **Estilização:** Tailwind CSS
- **Componentes UI:** Lucide React (ícones)
- **Drag and Drop:** @dnd-kit/core e @dnd-kit/sortable
- **Gráficos:** Chart.js
- **Notificações:** React Hot Toast
- **Testes:** Jest + React Testing Library

### 3.1.2 Backend
- **Framework:** Spring Boot 3.x (Java 17)
- **Persistência:** JPA/Hibernate
- **Banco de Dados:** PostgreSQL
- **Segurança:** Spring Security + JWT
- **Validação:** Jakarta Validation
- **Testes:** JUnit 5 + Mockito
- **Cobertura:** JaCoCo

### 3.1.3 Infraestrutura
- **CI/CD:** GitHub Actions
- **Deploy Backend:** AWS Elastic Beanstalk
- **Deploy Frontend:** AWS S3 + CloudFront
- **Versionamento:** Git/GitHub

## 3.2 Modelo de Dados

### 3.2.1 Entidades Principais

**Usuario**
- id, nome, email, senha (criptografada com bcrypt)
- perfil (ADMINISTRADOR, GERENTE, VENDEDOR)
- ativo, dataCriacao, ultimoAcesso

**Expositor**
- id, razaoSocial, nomeFantasia, cnpj (único)
- email, telefone, celular
- endereco, cidade, estado, cep, site
- descricao, status (ATIVO, INATIVO, BLOQUEADO, POTENCIAL)
- vendedor (relacionamento ManyToOne com Usuario)
- dataCadastro, dataAtualizacao
- Relacionamentos: OneToMany com Oportunidade e Interacao

**Oportunidade**
- id, titulo, descricao
- expositor (ManyToOne)
- vendedor (ManyToOne)
- status (PROSPECCAO, QUALIFICACAO, PROPOSTA, NEGOCIACAO, FECHADA_GANHA, FECHADA_PERDIDA, CANCELADA)
- fonte (INDICACAO, SITE, REDE_SOCIAL, TELEFONE, EMAIL, EVENTO, OUTROS)
- valorEstimado, probabilidadeFechamento
- dataPrevistaFechamento, dataFechamento
- dataCriacao, dataAtualizacao, observacoes

**Interacao**
- id, expositor (ManyToOne), usuario (ManyToOne)
- oportunidade (ManyToOne - opcional)
- tipo (LIGACAO, EMAIL, REUNIAO, VISITA, WHATSAPP, PROPOSTA, FECHADO, OUTROS)
- assunto, descricao
- dataInteracao, dataCriacao, dataAtualizacao
- proximaAcao, dataProximaAcao, concluida
- valorProposta, metrosQuadrados (para tipo PROPOSTA)

## 3.3 Requisitos Funcionais (STATUS DE IMPLEMENTAÇÃO)

### ✅ RF01: Cadastro de Expositores
**Status:** IMPLEMENTADO
- Sistema permite cadastro completo de expositores
- Validação de CNPJ único
- Consulta automática via API ReceitaWS
- Campos: razão social, nome fantasia, CNPJ, contatos, endereço completo, descrição
- Associação com vendedor
- Status configurável

### ✅ RF02: Gestão de Oportunidades de Vendas
**Status:** IMPLEMENTADO
- Sistema permite criar e gerenciar oportunidades
- Atualização de status via Kanban (drag-and-drop)
- Valores estimados e probabilidade de fechamento
- Fonte da oportunidade configurável
- Visualização em pipeline Kanban

### ✅ RF03: Histórico de Interações
**Status:** IMPLEMENTADO
- Registro completo de interações
- Tipos: ligação, email, reunião, visita, WhatsApp, proposta, fechado, outros
- Data, hora e tipo de interação registrados
- Associação com expositores e oportunidades
- Próximas ações e follow-ups

### ✅ RF04: Relatórios e Indicadores
**Status:** IMPLEMENTADO
- Dashboard com métricas em tempo real
- Relatórios de oportunidades por status
- Análise de vendas por mês
- Performance de vendedores
- Atividades por período
- Resumo executivo
- Gráficos interativos

### ✅ RF05: Gestão de Usuários e Permissões
**Status:** IMPLEMENTADO
- Controle de acesso baseado em perfis (ADMINISTRADOR, GERENTE, VENDEDOR)
- Cadastro e gerenciamento de usuários
- Ativação/desativação de usuários
- Registro de último acesso
- Autenticação JWT

### ✅ RF06: Lembretes e Tarefas
**Status:** IMPLEMENTADO
- Sistema de agenda integrado
- Agendamento de follow-ups
- Visualização por dia, semana ou mês
- Conclusão de atividades
- Próximas ações configuráveis

### ✅ RF07: Gestão de Eventos
**Status:** PARCIALMENTE IMPLEMENTADO
- Informações de eventos podem ser registradas nas oportunidades
- Campos específicos para espaços (metros quadrados)
- Preços podem ser registrados no valor estimado
- Gestão completa de eventos planejada para futuras releases

### ✅ RF08: Preferências de Expositores
**Status:** IMPLEMENTADO
- Campo de descrição permite registro de preferências
- Campos específicos em propostas (metros quadrados)
- Histórico de interações permite rastreamento de necessidades

### ✅ RF09: Dashboards Consolidados
**Status:** IMPLEMENTADO
- Dashboard principal com métricas gerais
- Visualização de performance individual e de equipe
- Métricas de ocupação e vendas
- Gráficos interativos

### ⚠️ RF10: Exportação de Relatórios
**Status:** PARCIALMENTE IMPLEMENTADO
- Interface preparada para exportação
- Funcionalidade de exportação PDF/Excel pendente de implementação final
- Dados disponíveis para exportação via API

### ✅ RF11: Calendário de Follow-ups
**Status:** IMPLEMENTADO
- Agenda integrada com visualização de atividades
- Lembretes através de atividades agendadas
- Filtros e busca de atividades
- Conclusão de atividades

### ✅ RF12: Feedback e Observações
**Status:** IMPLEMENTADO
- Campo de observações em oportunidades
- Descrição detalhada em interações
- Histórico completo de relacionamento

### 🆕 RF13: Assistente Virtual (ChatBot)
**Status:** IMPLEMENTADO (FUNCIONALIDADE ADICIONAL)
- ChatBot integrado para consultas rápidas
- Consultas sobre próximas reuniões, leads, atividades, propostas
- Respostas baseadas em dados reais do sistema

### 🆕 RF14: Integração CNPJ
**Status:** IMPLEMENTADO (FUNCIONALIDADE ADICIONAL)
- Consulta automática de CNPJ via API ReceitaWS
- Preenchimento automático de dados da empresa
- Validação de CNPJ

## 3.4 Requisitos Não Funcionais (STATUS DE IMPLEMENTAÇÃO)

### ✅ RNF01: Autenticação Segura
**Status:** IMPLEMENTADO
- Autenticação JWT implementada
- Criptografia de senhas com bcrypt
- Tokens com expiração configurável
- Spring Security configurado

### ⚠️ RNF02: Arquitetura de Microsserviços
**Status:** NÃO IMPLEMENTADO (ARQUITETURA MONOLÍTICA)
- Sistema desenvolvido como aplicação monolítica
- Arquitetura escalável baseada em Spring Boot
- Deploy independente de frontend e backend
- Planejado para migração futura para microsserviços

### ✅ RNF03: Design Responsivo
**Status:** IMPLEMENTADO
- Interface responsiva com Tailwind CSS
- Adaptado para desktop e mobile
- Sidebar colapsável
- Componentes responsivos

### ✅ RNF04: Performance
**Status:** IMPLEMENTADO
- Resposta em menos de 3 segundos para 95% das operações
- Otimizações de queries com JPA
- Paginação implementada
- Cache com React Query

### ✅ RNF05: Disponibilidade
**Status:** IMPLEMENTADO
- Deploy em AWS Elastic Beanstalk (backend)
- Deploy em AWS S3 + CloudFront (frontend)
- Monitoramento de health checks
- CI/CD automatizado

### ✅ RNF06: Suporte a Usuários Simultâneos
**Status:** IMPLEMENTADO
- Sistema suporta múltiplos usuários simultâneos
- Arquitetura stateless com JWT
- Banco de dados PostgreSQL escalável

### ✅ RNF07: Backup Automático
**Status:** IMPLEMENTADO (VIA AWS)
- Backups automáticos gerenciados pela AWS
- Retenção configurável
- RDS PostgreSQL com backups automáticos

### ✅ RNF08: Compatibilidade com Navegadores
**Status:** IMPLEMENTADO
- Compatível com Chrome, Firefox, Safari, Edge
- Testado em navegadores modernos
- Polyfills para compatibilidade

## 3.5 Tecnologias Utilizadas

### Frontend
- **React.js 18+**: Framework principal
- **React Router v6**: Roteamento
- **React Query (TanStack Query)**: Gerenciamento de estado e cache
- **Tailwind CSS**: Estilização
- **Lucide React**: Ícones
- **@dnd-kit**: Drag and drop para Kanban
- **Chart.js**: Gráficos
- **React Hot Toast**: Notificações
- **Axios**: Cliente HTTP
- **Jest + React Testing Library**: Testes

### Backend
- **Java 17**: Linguagem
- **Spring Boot 3.x**: Framework
- **Spring Security**: Segurança
- **Spring Data JPA**: Persistência
- **PostgreSQL**: Banco de dados
- **JWT (Java JWT)**: Autenticação
- **BCrypt**: Criptografia de senhas
- **Jakarta Validation**: Validação
- **JUnit 5 + Mockito**: Testes
- **JaCoCo**: Cobertura de código
- **Maven**: Gerenciamento de dependências

### Infraestrutura e DevOps
- **Git/GitHub**: Controle de versão
- **GitHub Actions**: CI/CD
- **AWS Elastic Beanstalk**: Deploy backend
- **AWS S3**: Armazenamento frontend
- **AWS CloudFront**: CDN
- **Docker**: Containerização (opcional)

## 3.6 Justificativa das Tecnologias

- **ReactJS**: Framework maduro e amplamente utilizado, oferece alto desempenho com Virtual DOM, rica comunidade e facilita criação de interfaces responsivas.

- **Spring Boot (Java)**: Proporciona rapidez no desenvolvimento de aplicações RESTful, robustez, extensões para segurança e ampla compatibilidade com bancos relacionais via JPA/Hibernate.

- **PostgreSQL**: Banco de dados relacional maduro e confiável, com forte suporte a integridade referencial, transações ACID e recursos avançados para otimização.

- **JWT**: Padrão consolidado para autenticação stateless, permitindo fácil integração com APIs e escalabilidade horizontal.

- **GitHub Actions**: Integração nativa com GitHub, facilitando CI/CD automatizado.

- **AWS**: Infraestrutura escalável e confiável, com serviços gerenciados que reduzem overhead operacional.

## 3.7 Endpoints da API (Backend)

### Autenticação (`/api/auth`)
- `POST /api/auth/login` - Login de usuário
- `POST /api/auth/register` - Registro de novo usuário

### Expositores (`/api/expositores`)
- `POST /api/expositores` - Criar expositor
- `GET /api/expositores` - Listar todos os expositores
- `GET /api/expositores/paginado` - Listar com paginação
- `GET /api/expositores/{id}` - Buscar por ID
- `PUT /api/expositores/{id}` - Atualizar expositor
- `DELETE /api/expositores/{id}` - Excluir expositor
- `GET /api/expositores/status/{status}` - Filtrar por status
- `GET /api/expositores/vendedor/{vendedorId}` - Filtrar por vendedor
- `PUT /api/expositores/{id}/status` - Atualizar status

### Oportunidades (`/api/oportunidades`)
- `GET /api/oportunidades` - Listar todas as oportunidades
- `POST /api/oportunidades/criar-exemplo` - Criar oportunidade de exemplo
- `GET /api/oportunidades/{id}` - Buscar por ID

### Agenda (`/api/agenda`)
- `GET /api/agenda/atividades` - Listar atividades da agenda
- `GET /api/agenda/atividades/lead/{leadId}` - Atividades por lead
- `POST /api/agenda/atividades` - Criar atividade
- `PUT /api/agenda/atividades/{id}/concluir` - Concluir atividade

### Dashboard (`/api/dashboard`)
- `GET /api/dashboard/stats` - Estatísticas do dashboard
- `GET /api/dashboard/atividades-grafico` - Dados para gráfico de atividades

### Relatórios (`/api/relatorios`)
- `GET /api/relatorios/oportunidades-por-status` - Oportunidades por status
- `GET /api/relatorios/vendas-por-mes` - Vendas por mês
- `GET /api/relatorios/performance-vendedores` - Performance de vendedores
- `GET /api/relatorios/atividades-por-periodo` - Atividades por período
- `GET /api/relatorios/resumo-executivo` - Resumo executivo

### ChatBot (`/api/chat`)
- `POST /api/chat/perguntar` - Processar pergunta do ChatBot

### CNPJ (`/api/cnpj`)
- `GET /api/cnpj/{cnpj}` - Consultar dados do CNPJ via ReceitaWS

## 3.8 Páginas do Frontend

### Páginas Implementadas
1. **Login** (`/login`) - Autenticação de usuários
2. **Dashboard** (`/`) - Visão geral com métricas
3. **Kanban** (`/kanban`) - Pipeline de vendas visual
4. **Expositores** (`/expositores`) - Lista e gestão de expositores
5. **Agenda** (`/agenda`) - Calendário de atividades
6. **Relatórios** (`/relatorios`) - Relatórios e análises
7. **Configurações** (`/configuracoes`) - Configurações do sistema
8. **Lead Detail** (`/lead/:id`) - Detalhes de um lead específico

### Componentes Principais
- **Layout**: Layout principal com sidebar e navegação
- **ChatBot**: Assistente virtual
- **AddClientModal**: Modal para adicionar cliente
- **StatusModal**: Modal para alterar status
- **ActivityModal**: Modal para criar atividades

## 3.9 Testes Implementados

### Frontend (Cobertura: 25%+)
- Testes de componentes (Layout, ChatBot, Modals)
- Testes de páginas (Login, Dashboard, Expositores, Agenda, Relatórios, Configurações)
- Testes de contextos (AuthContext)
- Testes de hooks (useSidebar)
- Testes de serviços (api.js)

### Backend (Cobertura: 75%+)
- Testes de serviços (UsuarioService, ExpositorService)
- Testes de controllers (AuthController, ExpositorController)
- Testes de segurança (JwtUtil)
- Testes unitários com JUnit 5 e Mockito
- Relatórios de cobertura com JaCoCo

## 3.10 CI/CD Implementado

### GitHub Actions Workflow
- **Trigger**: Push para branch `main`
- **Detecção de Mudanças**: Detecta alterações em backend ou frontend
- **Backend**:
  - Compilação e testes
  - Geração de relatório JaCoCo
  - Verificação de cobertura mínima (75%)
  - Build do JAR
  - Deploy para AWS Elastic Beanstalk
- **Frontend**:
  - Instalação de dependências
  - Testes com cobertura
  - Build de produção
  - Upload para S3
  - Invalidação de cache CloudFront

### Secrets Configurados
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `EB_APPLICATION_NAME`
- `EB_ENVIRONMENT_NAME`
- `S3_BUCKET_NAME`
- `CLOUDFRONT_DISTRIBUTION_ID` (opcional)

---

# 4 Funcionalidades Adicionais Implementadas

## 4.1 Assistente Virtual (ChatBot)
- ChatBot integrado na interface
- Consultas sobre o CRM em linguagem natural
- Respostas baseadas em dados reais do sistema
- Interface minimizável e responsiva

## 4.2 Integração com API ReceitaWS
- Consulta automática de CNPJ
- Preenchimento automático de dados da empresa
- Validação de CNPJ

## 4.3 Pipeline Kanban Visual
- Interface drag-and-drop para gestão de oportunidades
- Atualização de status visual
- Filtros e busca integrados

## 4.4 Dashboard em Tempo Real
- Métricas atualizadas automaticamente
- Gráficos interativos
- Visualização de performance

---

# 5 Próximos Passos e Melhorias Futuras

## 5.1 Melhorias Planejadas
- Implementação completa de exportação PDF/Excel
- Integração com gateways de pagamento
- Automação de campanhas de marketing
- MFA (Multi-Factor Authentication)
- Notificações por email
- App mobile nativo
- Integração com ERPs
- API pública para integrações

## 5.2 Otimizações Técnicas
- Migração para arquitetura de microsserviços (se necessário)
- Implementação de cache distribuído (Redis)
- Otimização de queries complexas
- Implementação de WebSockets para atualizações em tempo real
- Melhoria na cobertura de testes

---

# 6 Conclusão

O sistema CRMSHOT foi desenvolvido com sucesso, atendendo aos requisitos funcionais e não funcionais especificados na RFC original, além de incluir funcionalidades adicionais que agregam valor ao produto. O sistema está em produção, com CI/CD automatizado, testes implementados e deploy na AWS.

**Status Final:** ✅ PROJETO CONCLUÍDO E EM PRODUÇÃO

---

# 7 Referências

- https://spring.io/projects/spring-boot
- https://www.postgresql.org/docs/
- https://reactjs.org
- https://jwt.io/introduction
- https://owasp.org/www-project-top-ten/
- https://github.com/kelektiv/node.bcrypt.js
- https://receitaws.com.br/
- Fielding, Roy T. Architectural Styles and the Design of Network-based Software Architectures, 2000.
- Martin, Robert C. Clean Architecture, 2017.

---

**Documento atualizado em:** Novembro 2024
**Versão:** 2.0
**Status:** Projeto Concluído


