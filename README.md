# Sistema CRM para Clientes

**Autor:** Leonardo Luís da Rocha  
**Data:** Abril de 2025

## Resumo

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) que permita o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial. A plataforma será projetada para otimizar a gestão do relacionamento com os clientes, garantindo um fluxo de trabalho mais organizado e transparente para a equipe de vendas.

---

## Introdução

### Contexto

A gestão de relacionamento com clientes é um fator crítico para o sucesso comercial de empresas em mercados competitivos. No entanto, muitas organizações ainda utilizam planilhas ou anotações manuais, resultando em perda de dados, dificuldades na comunicação interna e baixa eficiência no acompanhamento de clientes. A adoção de um sistema CRM visa resolver esses entraves por meio da automatização e centralização dos processos de vendas e atendimento ao cliente.

### Justificativa

A implementação de um sistema de CRM é essencial para promover uma gestão estruturada e inteligente do relacionamento com os clientes. Por meio dele, a equipe de vendas poderá acompanhar cada estágio do funil de vendas, realizar interações com registro histórico e obter indicadores de desempenho em tempo real. Diferente de soluções genéricas ou manuais, este projeto foca em uma proposta customizável, segura e com foco específico na melhoria da performance comercial.

---

## Objetivos

**Objetivo principal:**  
Desenvolver um sistema web de CRM que automatize o cadastro, acompanhamento e análise do relacionamento com clientes, desde o primeiro contato até o fechamento da venda.

**Objetivos específicos:**

- Levantar os requisitos funcionais e não funcionais do sistema.
- Especificar os módulos principais: cadastro, pipeline de vendas, histórico e relatórios.
- Modelar a base de dados e definir os fluxos de interação da aplicação.
- Desenvolver e integrar os componentes frontend e backend.
- Testar e validar a usabilidade e a segurança do sistema.

---

## Descrição do Projeto

### Tema do Projeto

Sistema de CRM focado na gestão do relacionamento com clientes, permitindo acompanhamento detalhado desde o primeiro contato até o fechamento da venda.

### Problemas a Resolver

- Falta de organização no acompanhamento dos clientes.
- Perda de oportunidades de vendas por ausência de registros estruturados.
- Dificuldade de comunicação e colaboração entre os membros da equipe de vendas.
- Inexistência de dados consolidados para suporte à decisão estratégica.

### Limitações

- A primeira versão não contemplará integrações com sistemas externos.
- O escopo inicial foca exclusivamente na área de vendas, sem automação de marketing.
- A segurança será aplicada com medidas básicas, com evolução planejada para fases futuras.

---

## Especificação Técnica

### Descrição da Proposta

Plataforma web responsiva com funcionalidades de cadastro, acompanhamento de pipeline de vendas e análise de desempenho.

### Requisitos de Software

#### Requisitos Funcionais

- **Cadastro de Clientes e Vendedores:** Registro detalhado (nome, e-mail, telefone, empresa, cargo, etc.) e associação com vendedores.
- **Gestão de Oportunidades:** Propostas comerciais associadas aos clientes; atualização de status; notificações automáticas.
- **Histórico de Interações:** Registro de chamadas, reuniões e e-mails; integração com calendários.
- **Relatórios e Análises:** Dashboard com indicadores de desempenho por vendedor; relatórios de conversão, tempo médio de fechamento e taxa de perda.
- **Gerenciamento de Usuários:** Controle de acesso baseado em perfis; registro de logs de atividades.

#### Requisitos Não Funcionais

- Segurança com JWT + bcrypt.
- Arquitetura escalável baseada em microsserviços.
- Design responsivo e acessível para desktop e mobile.

---

## Tecnologias Utilizadas

- **Frontend:** ReactJS  
- **Backend:** Java (Spring Boot)  
- **Banco de dados:** PostgreSQL  
- **Autenticação:** JWT  

---

## Considerações de Segurança

- Criptografia de senhas com bcrypt.
- Autenticação segura com JWT.
- Validação contra SQL Injection e XSS.
- Controle de acesso por perfis.
- Backup periódico com testes.
- Futuro: MFA, monitoramento de sessões, análise de vulnerabilidades.

---

## Próximos Passos

### Portfólio I

- Levantamento e modelagem de requisitos.
- Especificação de casos de uso.
- Modelagem de dados e fluxos.
- Desenvolvimento dos módulos iniciais.
- Testes unitários e validação parcial.

### Portfólio II

- Integração frontend/backend.
- Implementação de relatórios, histórico de interações e gerenciamento de usuários.
- Otimizações de performance e segurança.
- Testes finais de usabilidade.
- Documentação técnica e entrega final.

---

## Referências

- [Spring Boot](https://spring.io/projects/spring-boot)  
- [PostgreSQL Docs](https://www.postgresql.org/docs/)  
- [ReactJS](https://reactjs.org)  
- [JWT Introduction](https://jwt.io/introduction)  
- [OWASP Top Ten](https://owasp.org/www-project-top-ten/)  
- [bcrypt para Node](https://github.com/kelektiv/node.bcrypt.js)  
- Fielding, Roy T. *Architectural Styles and the Design of Network-based Software Architectures*, 2000.  
- Robert C. Martin. *Clean Architecture*, 2017.
