# 🚀 Carrier - Sistema Multi-Modular de Logística Reativa

O **Carrier** é uma plataforma distribuída de alta performance para o gerenciamento, solicitação e processamento de coletas logísticas em tempo real. Desenvolvido sob o ecossistema **Java 21** e **Spring Boot 3.x**, o projeto adota uma arquitetura multimodular (*Monorepo*) e orientada a eventos (*Event-Driven Architecture - EDA*), garantindo isolamento de dados, resiliência e alto throughput de mensagens.

---

## 🛠️ Stack Tecnológica

- **Core:** Java 21 & Spring Boot 3.x (Spring Data JPA, Spring Web)
- **Segurança:** Spring Security 6.x & Auth0 JWT (Bearer Token stateless)
- **Mensageria:** Apache Kafka (Modo KRaft - Sem Zookeeper)
- **Banco de Dados:** PostgreSQL 17 (Padrão *Database per Service*)
- **Containers & Infra:** Docker & Docker Compose
- **Build & Módulos:** Maven (Estrutura Multimodular)
- **Produtividade:** Lombok & Jakarta Validation

---

## 🏛️ Desenho Arquitetural & Fluxo de Dados

O sistema implementa o padrão **Database per Service**, onde cada microsserviço possui sua própria instância e esquema isolados do PostgreSQL, eliminando o acoplamento a nível de persistência. A comunicação core do ecossistema ocorre de forma híbrida e bidirecional através do Apache Kafka:

```text
[ Cliente Final ] ──( Autentica )──> [ Collection Módulo (Producer) ] ──> Grava no Postgres (Coleta)
                                                │
                                       ( Kafka Producer ) ──( JSON: CollectionSchema )
                                                │                               │
                                                ▼                               ▼
[ Módulo Original ] ◄──( String: NovoStatus )── [ Tópico: status-updates ] ◄── [ Tópico: Carrier-module. ]
                                                                                │
                                                                         ( Kafka Consumer )
                                                                                │
                                                                                ▼
[ Transportadora ] ◄─────────────────────────────────────────────── [ toRemove Módulo (Consumer) ]
```

1. **Fluxo de Ida (Solicitação):** O cliente final autenticado via HTTP solicita uma coleta. O módulo `Collection` persiste os dados localmente, transforma a entidade através de um Mapper e publica o payload complexo e autocontido `CollectionSchema` no tópico `Carrier-module.`.
2. **Fluxo de Volta (Operacional):** O módulo `toRemove` (Transportadora) consome o evento, realiza o achatamento de dados (*flattening*) para otimizar leituras locais em seu próprio banco de dados e, ao atualizar o andamento do frete, envia uma `String` leve de volta pelo tópico `carrier.collection.status-updates` para sincronizar o status no microsserviço original de forma 100% assíncrona.

---

## 💼 Destaque Estratégico para Currículo & Portfólio

> *Seção estruturada para recrutadores, Tech Leads e Engenheiros de Software Seniores.*

### 🛠️ Desafios de Engenharia Solucionados
*   **Quebra de Dependência Cíclica em Build Multimodular Maven:** Resolução de travamentos de compilação de pacotes `.jar` movendo a responsabilidade de transformação de entidades relacionais para fora do módulo `schema` (contrato puro) e isolando-a em classes `Mapper` estáticas dentro do microsserviço produtor.
*   **Prevenção de Vulnerabilidade de Intrusão Relacional (ID Spoofing):** Implementação de uma barreira defensiva na camada de serviço do Spring Boot. O sistema intercepta a rota de deleção e valida de forma estrita se o recurso solicitado pertence rigorosamente ao ID do usuário autenticado extraído do contexto seguro do `@AuthenticationPrincipal`.
*   **Garantia de Ordenação Sequencial Estrita via Kafka Partition Key:** Utilização do `UUID` da coleta como chave de partição (`Message Key`) no Kafka. Isso garante que todos os eventos e mudanças de status de uma mesma entrega sejam direcionados para a mesma partição do broker, evitando inconsistências de inversão de histórico no consumidor.
*   **Otimização de Banda com Mensageria Híbrida Leve:** Redução drástica do payload de sincronização reversa trafegando apenas strings puras via `StringSerializer` para atualizar o status do sistema de origem, evitando a redundância e o custo de rede de trafegar novos JSONs complexos.

### 📈 Decisões Arquiteturais de Alto Impacto
*   **Achatamento de Tabelas para Alta Performance de Leitura (*Read-heavy Optimization*):** O módulo consumidor (`toRemove`) desmancha os objetos aninhados do evento e armazena os dados de usuário e endereço de forma linear em uma única tabela densa. Isso zera a necessidade de `JOINs` complexos, garantindo buscas e listagens de fretes em tempo de CPU.
*   **Resiliência Avançada com Produtor Idempotente:** Configuração do ecossistema produtor com `enable.idempotence=true` e `acks=all`, eliminando o risco de duplicidade de registros no Kafka ou perda de mensagens causadas por oscilações temporárias de conexões de rede.

---

## 📦 Estrutura de Módulos (Monorepo)

O projeto está organizado como um projeto multimodular Maven, aplicando separação rígida de conceitos (*Separation of Concerns*):

*   **`schema/`**: Biblioteca Java centralizada, agnóstica e cross-project. Contém apenas os contratos comuns, estruturas imutáveis (`records`) de transporte e enums compartilhados de status.
*   **`Collection/`**: Microsserviço *Producer/Consumer* responsável pelo fluxo de vendas, autenticação via filtro customizado JWT, cadastro de clientes, endereços paginados e publicação de mensagens no barramento.
*   **`toRemove/`**: Microsserviço *Consumer/Producer* da transportadora, encarregado da ingestão assíncrona de eventos, persistência em banco local isolado e devolução de atualizações de status.

---

## 🔐 Estratégia de Segurança Dinâmica (JWT Sem State)

Para mitigar a latência tradicional de rede de microsserviços consultando bancos de dados a cada requisição HTTP (*I/O Bound*), este projeto implementa um modelo **Stateless Descentralizado**:

As permissões e identificações do usuário do Spring Security são embutidas dentro do token durante a autenticação (`.withClaim`). O filtro customizado `SecurityFilter` intercepta as chamadas via `OncePerRequestFilter`, valida a assinatura criptográfica via chave secreta em memória e popula o `SecurityContextHolder`. **Validação ultra rápida em tempo de CPU, sem latência de rede.**

---

## 📨 Contrato de Evento Autocontido (`CollectionSchema`)

Contrato imutável unificado que trafega no barramento do Kafka através do módulo `schema`:

```java
public record CollectionSchema(
        UUID id,
        String item_name,
        Integer quantity,
        Double weight,
        CollectionStatus status,
        AddressDTO addressDTO,
        UserDTO userDTO) {
}
```

---

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
- Java 21 instalado
- Docker e Docker Compose ativos

### Passo 1: Subir a Infraestrutura Unificada (Banco e Kafka KRaft)
Acesse a raiz do projeto onde está o arquivo `docker-compose.yml` (corrigido para operar na mesma rede integrada `carrier-network`) e execute:
```bash
docker compose up -d
```

### Passo 2: Compilar os Módulos
Execute o build do projeto Maven na raiz do monorepo para gerar os artefatos do schema e dos serviços:
```bash
mvn clean install
```

### Passo 3: Executar as Aplicações
Inicialize as aplicações Spring Boot (`Collection` e `toRemove`). Graças às propriedades externas configuradas no `application.yml`, os serviços se conectarão automaticamente nas portas expostas (`5676` para o PostgreSQL e `9095` para o cluster KRaft do Kafka).

