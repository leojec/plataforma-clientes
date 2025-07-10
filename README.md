# CRMSHOT - CRM De Vendas
## de feiras e eventos

![Logo](/images/catolica.png)

**Centro Universitário Católica de Santa Catarina - Joinville**  
Engenharia de Software  
Leonardo Luis da Rocha

Julho de 2025

---

## Resumo

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) para o evento Shot Fair Brasil que permita o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial. A plataforma será projetada para otimizar a gestão do relacionamento com os clientes, garantindo um fluxo de trabalho mais organizado e transparente para a equipe de vendas.

---

## Introdução

### Contexto

A gestão de relacionamento com clientes é um fator crítico para o sucesso comercial de empresas em mercados competitivos. No entanto, muitas organizações ainda utilizam planilhas ou anotações manuais, resultando em perda de dados, dificuldades na comunicação interna e baixa eficiência no acompanhamento de clientes. A adoção de um sistema CRM visa resolver esses entraves por meio da automatização e centralização dos processos de vendas e atendimento ao cliente.

### Justificativa

A implementação de um sistema de CRM é essencial para promover uma gestão estruturada e inteligente do relacionamento com os clientes. Por meio dele, a equipe de vendas poderá acompanhar cada estágio do funil de vendas, realizar interações com registro histórico e obter indicadores de desempenho em tempo real. Diferente de soluções genéricas ou manuais, este projeto foca em uma proposta customizável, segura e com foco específico na melhoria da performance comercial.

### Objetivos

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

Desenvolvimento de um sistema de CRM para empresas que buscam otimizar o acompanhamento e a gestão do relacionamento com clientes e prospects. O sistema será uma plataforma web acessível por navegadores modernos, com interface intuitiva, adaptada tanto para desktops quanto dispositivos móveis.

### Escopo Funcional

O sistema CRM permitirá:
- O cadastro e gestão de expositores, incluindo dados corporativos, responsáveis, histórico de participação em eventos e preferências.
- O acompanhamento do pipeline de vendas, desde o primeiro contato até o fechamento do negócio, com etapas configuráveis.
- O registro detalhado de interações entre o time de vendas e os expositores (ligações, e-mails, reuniões, visitas), permitindo rastreabilidade completa.
- A configuração de lembretes, tarefas e follow-ups automáticos.
- A geração de relatórios gerenciais e dashboards para análise do funil de vendas, tempo médio de conversão, taxa de perdas e taxa de ocupação de eventos.
- O controle de usuários com diferentes perfis (administrador, gerente, vendedor), incluindo gestão de permissões e auditoria de operações.

### Problemas a Resolver

- Falta de centralização das informações, dificultando a visão completa do cliente.
- Alto risco de perda de oportunidades pela ausência de lembretes e rastreabilidade.
- Dificuldade para gerentes visualizarem a performance da equipe em tempo real.
- Complexidade na consolidação de dados históricos para entender padrões de comportamento dos expositores.

### Diferenciais do Projeto

- Acompanhamento granular de preferências dos expositores (stand, localização, recursos adicionais).
- Visualização e gestão da ocupação dos espaços por evento.
- Exportação de relatórios em PDF e Excel.

### Limitações

- Não incluirá integrações diretas com gateways de pagamento ou ERPs legados na primeira versão.
- Não contemplará automação de campanhas de marketing digital neste release.
- Segurança inicial baseada em autenticação e criptografia, com MFA e monitoramento avançado para futuras iterações.

---

## Especificação Técnica

### Modelagem C4

A modelagem C4 é uma abordagem para descrever a arquitetura de software por meio de uma hierarquia de diagramas: Contexto, Contêiner, Componente e Código.

![C4](images/tccimg.drawio.png)

---

### Diagramas de Caso de Uso

#### Vendedor

O vendedor cadastra e gerencia expositores, controla o pipeline de vendas, registra interações, configura lembretes, gera relatórios, exporta dados e gerencia follow-ups.

![Caso de Uso - Vendedor](images/vendedorcuso.png)

#### Administrador

Gerencia usuários, permissões, auditoria e visualiza dashboards consolidados.

![Caso de Uso - Administrador](images/admcuso.png)

---

## Requisitos Funcionais

- **RF01:** Cadastro de expositores com dados completos.
- **RF02:** Gestão de oportunidades de vendas com pipeline.
- **RF03:** Registro de histórico de interações.
- **RF04:** Visualização de relatórios e indicadores.
- **RF05:** Gerenciamento de usuários e permissões.
- **RF06:** Configuração de lembretes e follow-ups.
- **RF07:** Gestão de informações específicas de eventos.
- **RF08:** Registro de preferências dos expositores.
- **RF09:** Dashboards consolidados para gerentes.
- **RF10:** Exportação de relatórios em PDF/Excel.
- **RF11:** Gerenciamento de calendário de follow-ups.
- **RF12:** Registro de feedbacks e observações.

---

## Requisitos Não Funcionais

- **RNF01:** Autenticação segura com JWT e senhas criptografadas.
- **RNF02:** Arquitetura escalável baseada em microsserviços.
- **RNF03:** Design responsivo para desktop e mobile.
- **RNF04:** Resposta em até 3s para 95% das operações.
- **RNF05:** Disponibilidade mínima de 99%.
- **RNF06:** Suporte para até 50 usuários simultâneos.
- **RNF07:** Backup diário com retenção de 30 dias.
- **RNF08:** Compatível com navegadores modernos.

---

## Tecnologias Utilizadas

- **Frontend:** ReactJS
- **Backend:** Java (Spring Boot)
- **Banco de Dados:** PostgreSQL
- **Autenticação:** JWT

### Justificativas

- **ReactJS:** Performance via Virtual DOM, responsividade e grande comunidade.
- **Spring Boot:** Rápida criação de APIs REST, segurança robusta e suporte a JPA.
- **PostgreSQL:** Integridade referencial, suporte a JSONB, ACID e consultas avançadas.
- **JWT:** Autenticação stateless, ideal para escalabilidade horizontal.

---

## Próximos Passos

### Portfólio I
- Levantamento e modelagem de requisitos.
- Especificação de casos de uso.
- Modelagem de dados e fluxos.
- Desenvolvimento inicial.
- Testes unitários e validação parcial.

### Portfólio II
- Integração frontend/backend.
- Implementação de relatórios e gerenciamento avançado.
- Otimizações de performance e segurança.
- Testes finais de usabilidade.
- Documentação técnica e entrega final.

---

## Referências

- https://spring.io/projects/spring-boot  
- https://www.postgresql.org/docs/  
- https://reactjs.org  
- https://jwt.io/introduction  
- https://owasp.org/www-project-top-ten/  
- https://github.com/kelektiv/node.bcrypt.js  
- Fielding, Roy T. *Architectural Styles and the Design of Network-based Software Architectures*, 2000.  
- Martin, Robert C. *Clean Architecture*, 2017.

---

## Avaliações

- Assinatura Prof Edicarsia
- Assinatura Prof Camargo
- Assinatura Prof Manseira
