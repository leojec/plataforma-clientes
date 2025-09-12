# CRMSHOT - CRM De Vendas

**Centro Universitário Católica de Santa Catarina - Joinville**  
Engenharia de Software  
Leonardo Luis da Rocha

Julho de 2025

---

## Resumo

O projeto consiste no desenvolvimento de um sistema de CRM (Customer Relationship Management) para o evento Shot Fair Brasil que permita o cadastro e o acompanhamento eficiente de clientes ao longo de todo o processo comercial. A plataforma será projetada para otimizar a gestão do relacionamento com os clientes, garantindo um fluxo de trabalho mais organizado e transparente para a equipe de vendas.

---

## 🚀 Tecnologias Utilizadas

- **Frontend:** ReactJS 18 + Tailwind CSS
- **Backend:** Java 17 + Spring Boot 3.2
- **Banco de Dados:** PostgreSQL 15
- **Autenticação:** JWT (JSON Web Tokens)
- **Build Tools:** Maven (Backend) + NPM (Frontend)

### Justificativas

- **ReactJS:** Performance via Virtual DOM, responsividade e grande comunidade.
- **Spring Boot:** Rápida criação de APIs REST, segurança robusta e suporte a JPA.
- **PostgreSQL:** Integridade referencial, suporte a JSONB, ACID e consultas avançadas.
- **JWT:** Autenticação stateless, ideal para escalabilidade horizontal.

---

## 📁 Estrutura do Projeto

```
TCC/
├── backend/                 # API Spring Boot
│   ├── src/main/java/com/crmshot/
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios JPA
│   │   ├── service/        # Lógica de negócio
│   │   ├── controller/     # Controllers REST
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── config/        # Configurações
│   │   └── security/      # Configurações de segurança
│   ├── src/main/resources/
│   │   └── application.yml # Configurações da aplicação
│   └── pom.xml            # Dependências Maven
├── frontend/               # Aplicação React
│   ├── src/
│   │   ├── components/    # Componentes React
│   │   ├── pages/         # Páginas da aplicação
│   │   ├── services/      # Serviços de API
│   │   ├── contexts/      # Contextos React
│   │   └── utils/         # Utilitários
│   ├── public/            # Arquivos estáticos
│   └── package.json       # Dependências NPM
├── database/              # Scripts de banco de dados
│   ├── init.sql          # Script de inicialização
│   └── sample_data.sql   # Dados de exemplo
├── scripts/              # Scripts de configuração
│   └── setup.sh         # Script de setup do ambiente
└── docs/                # Documentação
    ├── *.png            # Diagramas e imagens
    └── images/          # Outras imagens
```

---

## 🛠️ Configuração do Ambiente

### Pré-requisitos

- Java 17 ou superior
- Node.js 16 ou superior
- Docker e Docker Compose
- Maven 3.6 ou superior
- PostgreSQL 15 (ou Docker)

### Instalação Rápida

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/leojec/plataforma-clientes.git
   cd plataforma-clientes
   ```

2. **Execute o script de configuração:**
   ```bash
   ./scripts/setup.sh
   ```

3. **Inicie o backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Inicie o frontend (em outro terminal):**
   ```bash
   cd frontend
   npm start
   ```

### Acesso ao Sistema

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **Banco de dados:** localhost:5432

### Usuários de Teste

- **Administrador:** admin@crmshot.com / admin123
- **Vendedor:** joao@crmshot.com / admin123
- **Vendedor:** maria@crmshot.com / admin123

---

## 📋 Funcionalidades Implementadas

### ✅ Módulos Principais

- **Autenticação e Autorização**
  - Login com JWT
  - Controle de acesso por perfis (Admin, Gerente, Vendedor)
  - Middleware de autenticação

- **Gestão de Expositores**
  - Cadastro completo de expositores
  - Busca e filtros avançados
  - Controle de status (Ativo, Inativo, Bloqueado, Potencial)
  - Associação com vendedores

- **Pipeline de Oportunidades**
  - Criação e gestão de oportunidades
  - Controle de status do funil de vendas
  - Acompanhamento de valores e probabilidades
  - Previsão de fechamento

- **Histórico de Interações**
  - Registro de ligações, emails, reuniões
  - Sistema de follow-ups
  - Rastreabilidade completa

- **Dashboard e Relatórios**
  - Visão geral do negócio
  - Gráficos de performance
  - Relatórios por vendedor
  - Análise de conversão

### 🔄 Status do Desenvolvimento

- [x] Estrutura base do projeto
- [x] Configuração do banco de dados
- [x] Entidades JPA principais
- [x] APIs REST básicas
- [x] Interface React responsiva
- [x] Sistema de autenticação
- [x] Dashboard principal
- [ ] Testes automatizados
- [ ] Deploy em produção
- [ ] Documentação da API

---

## 🗄️ Modelo de Dados

### Entidades Principais

1. **Usuario** - Usuários do sistema (Admin, Gerente, Vendedor)
2. **Expositor** - Clientes/Expositores do evento
3. **Oportunidade** - Oportunidades de vendas no pipeline
4. **Interacao** - Histórico de interações com clientes

### Relacionamentos

- Usuario → Expositor (1:N) - Vendedor responsável
- Expositor → Oportunidade (1:N) - Oportunidades do expositor
- Usuario → Oportunidade (1:N) - Vendedor da oportunidade
- Expositor → Interacao (1:N) - Interações com o expositor
- Oportunidade → Interacao (1:N) - Interações da oportunidade

---

## 🔒 Segurança

- Autenticação JWT com expiração configurável
- Senhas criptografadas com BCrypt
- CORS configurado para desenvolvimento
- Validação de dados com Bean Validation
- Controle de acesso baseado em perfis

---

## 📊 APIs Principais

### Autenticação
- `POST /api/auth/login` - Login do usuário
- `POST /api/auth/register` - Registro de usuário

### Expositores
- `GET /api/expositores` - Listar expositores
- `POST /api/expositores` - Criar expositor
- `GET /api/expositores/{id}` - Buscar expositor
- `PUT /api/expositores/{id}` - Atualizar expositor
- `DELETE /api/expositores/{id}` - Excluir expositor

### Oportunidades
- `GET /api/oportunidades` - Listar oportunidades
- `POST /api/oportunidades` - Criar oportunidade
- `PUT /api/oportunidades/{id}` - Atualizar oportunidade

### Interações
- `GET /api/interacoes` - Listar interações
- `POST /api/interacoes` - Criar interação

---

## 🧪 Testes

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

---

## 📈 Próximos Passos

### Portfólio I
- [x] Levantamento e modelagem de requisitos
- [x] Especificação de casos de uso
- [x] Modelagem de dados e fluxos
- [x] Desenvolvimento inicial
- [ ] Testes unitários e validação parcial

### Portfólio II
- [ ] Integração frontend/backend completa
- [ ] Implementação de relatórios avançados
- [ ] Otimizações de performance e segurança
- [ ] Testes finais de usabilidade
- [ ] Documentação técnica e entrega final

---

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 📞 Contato

**Leonardo Luis da Rocha**  
Email: leonardo.rocha@estudante.catolicasc.org.br  
LinkedIn: [leonardo-rocha](https://linkedin.com/in/leonardo-rocha)

---

## 📚 Referências

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://reactjs.org)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT.io](https://jwt.io/introduction)
- [Tailwind CSS](https://tailwindcss.com)

---

## Avaliações

- Assinatura Prof Edicarsia
- Assinatura Prof Camargo  
- Assinatura Prof Manseira