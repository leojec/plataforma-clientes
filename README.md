# Sistema CRM para Clientes

**Autor:** Leonardo Luís da Rocha  
**Data:** Abril de 2025

---

## Resumo

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) que permita o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial. A plataforma será projetada para otimizar a gestão do relacionamento com os clientes, garantindo um fluxo de trabalho mais organizado e transparente para a equipe de vendas.

---

## 1. Introdução

### 1.1 Contexto

O projeto se insere no contexto da gestão comercial, onde empresas precisam acompanhar de forma eficiente seus clientes ao longo de todo o ciclo de vendas. A falta de um sistema bem estruturado pode resultar em perda de oportunidades, comunicação desorganizada e menor conversão de vendas.

### 1.2 Justificativa

Sistemas de CRM desempenham um papel fundamental na otimização de processos empresariais. Um CRM bem projetado permite que equipes de vendas tenham um fluxo de trabalho mais eficiente, facilitando a captação e fidelização de clientes.

### 1.3 Objetivos

**Objetivo principal:**  
Desenvolver um sistema de CRM que permita o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial.

**Objetivos secundários:**

- Melhorar a organização e a transparência da equipe de vendas.
- Reduzir a perda de leads e aumentar a conversão de vendas.
- Trazer um feedback do andamento de contatos com clientes.

---

## 2. Descrição do Projeto

### 2.1 Tema do Projeto

Sistema de CRM focado na gestão do relacionamento com clientes, permitindo acompanhamento detalhado do cliente desde o primeiro contato até o fechamento da venda.

### 2.2 Problemas a Resolver

- Falta de organização no acompanhamento dos clientes.
- Perda de oportunidades de vendas por falta de um registro adequado.
- Dificuldade na comunicação e colaboração da equipe de vendas.
- Falta de dados estruturados para análise e decisões estratégicas.

### 2.3 Limitações

- Sem integrações externas na primeira versão.
- Foco no fluxo de vendas, sem automação de marketing inicial.
- Segurança tratada apenas com medidas básicas na primeira fase.

---

## 3. Especificação Técnica

### 3.1 Descrição da Proposta

Plataforma web responsiva com cadastro, acompanhamento e análise do pipeline de vendas.

### 3.2 Requisitos de Software

#### Requisitos Funcionais

**Cadastro de Clientes**
- Registro detalhado (nome, e-mail, telefone, empresa, etc.).
- Classificação por status (lead, prospect, ativo, inativo).

**Gestão de Oportunidades**
- Propostas comerciais vinculadas ao cliente.
- Atualização de status (aberta, negociação, fechada, perdida).
- Notificações automáticas.

**Relatórios e Análises**
- Dashboard com desempenho da equipe.
- Relatórios de conversão e tempo de fechamento.

**Gerenciamento de Usuários**
- Controle por níveis de permissão.
- Registro de atividades por usuário.

**Histórico de Interações**
- Registro de chamadas, e-mails e reuniões.
- Integração com calendários.

#### Requisitos Não Funcionais

- **Segurança:** JWT + bcrypt.
- **Escalabilidade:** Microservices.
- **Usabilidade:** Responsivo e intuitivo (desktop/mobile).

### 3.3 Tecnologias Utilizadas

**Frontend**
- ReactJS

**Backend**
- Java (Spring Boot)
- PostgreSQL
- Autenticação: JWT

### 3.4 Considerações de Segurança

- Criptografia de senhas com bcrypt.
- Autenticação segura com JWT.
- Validação contra SQL Injection e XSS.
- Controle de acesso por perfis.
- Backup periódico com testes.
- Futuro: MFA, monitoramento de sessões, análise de vulnerabilidades.

---

## 4. Próximos Passos

### 4.1 Portfólio I

- Requisitos e casos de uso detalhados.
- Modelagem de dados e estrutura.
- Módulos iniciais: cadastro e gestão de oportunidades.
- Interface inicial (UI/UX).
- Testes unitários.

### 4.2 Portfólio II

- Integração frontend/backend.
- Módulos: relatórios, histórico, controle de usuários.
- Refino de segurança e performance.
- Testes de usabilidade.
- Documentação final e entrega.

---

## 5. Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [ReactJS](https://reactjs.org)
- [JWT Introduction](https://jwt.io/introduction)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Bcrypt - Node.js](https://github.com/kelektiv/node.bcrypt.js)
- Fielding, Roy T. *Architectural Styles and the Design of Network-based Software Architectures*, 2000.
- Robert C. Martin. *Clean Architecture*, 2017.


