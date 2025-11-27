# Como Gerar Relatório de Cobertura de Testes - Backend

## Opção 1: Gerar Relatório Completo (Recomendado)

Execute os testes e gere o relatório em um único comando:

```bash
cd /Users/leonardorocha/Documents/TCC/projeto/backend
mvn clean test jacoco:report
```

Este comando irá:
1. Limpar arquivos anteriores (`clean`)
2. Executar todos os testes (`test`)
3. Gerar o relatório HTML de cobertura (`jacoco:report`)

## Opção 2: Apenas Gerar o Relatório (Se os testes já foram executados)

Se você já executou os testes anteriormente, pode gerar apenas o relatório:

```bash
cd /Users/leonardorocha/Documents/TCC/projeto/backend
mvn jacoco:report
```

## Opção 3: Verificar Cobertura Mínima (75%)

Para executar os testes e verificar se a cobertura está acima de 75%:

```bash
cd /Users/leonardorocha/Documents/TCC/projeto/backend
mvn clean test jacoco:check
```

## Onde Encontrar o Relatório

Após executar o comando, o relatório HTML estará disponível em:

```
projeto/backend/target/site/jacoco/index.html
```

**Para abrir no navegador:**

### No macOS:
```bash
open target/site/jacoco/index.html
```

### No Linux:
```bash
xdg-open target/site/jacoco/index.html
```

### No Windows:
```bash
start target/site/jacoco/index.html
```

## Estrutura do Relatório

O relatório HTML mostra:
- **Visão geral** por pacote (controller, service, repository, etc.)
- **Cobertura de instruções** (instructions)
- **Cobertura de branches** (ramificações)
- **Cobertura de linhas** (lines)
- **Cobertura de métodos** (methods)
- **Cobertura de classes** (classes)

Você pode navegar pelos pacotes e ver detalhes de cada classe, incluindo:
- Código fonte com destaque de linhas cobertas (verde) e não cobertas (vermelho)
- Métricas detalhadas por método
- Gráficos de cobertura

## Relatórios Adicionais

O JaCoCo também gera outros formatos em `target/site/jacoco/`:

- **jacoco.xml**: Formato XML para integração com ferramentas CI/CD
- **jacoco.csv**: Formato CSV para análise em planilhas
- **jacoco-sessions.html**: Informações sobre as sessões de teste

## Verificação de Cobertura Mínima no CI/CD

No GitHub Actions, a verificação de cobertura mínima de 75% já está configurada automaticamente. O relatório também é enviado como artifact.

## Comandos Úteis

### Ver apenas o resumo da cobertura no terminal:
```bash
mvn test jacoco:report
cat target/site/jacoco/index.html | grep -A 5 "Total"
```

### Limpar e gerar relatório completo:
```bash
mvn clean test jacoco:report
```

### Gerar relatório mesmo se alguns testes falharem:
```bash
mvn test jacoco:report -Dmaven.test.failure.ignore=true
```

## Configuração Atual

- **Cobertura mínima exigida**: 75% (configurado no `pom.xml`)
- **Plugin JaCoCo**: versão 0.8.11
- **Métrica verificada**: LINHA (LINE coverage)

## Dica

Para visualizar o relatório rapidamente após gerá-lo:

```bash
# Gerar relatório e abrir automaticamente no navegador
mvn clean test jacoco:report && open target/site/jacoco/index.html
```

---

**Observação**: O relatório já foi gerado anteriormente e está disponível em `target/site/jacoco/index.html`. Se você fez alterações no código ou nos testes, execute `mvn clean test jacoco:report` novamente para atualizar o relatório.

