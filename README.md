# Projeto de Análise de Veículos

Atualizações implementadas:

1. Serviço de consulta de VIN com cache Caffeine (resolve placa/renavam -> VIN stub)
- `VinLookupService`

2. Clientes externos
- F1: Skeleton de cliente estilo SOAP (WebServiceTemplate) com Resilience4j retry
- F2/F3: Clientes baseados em WebClient com Resilience4j retry e URLs compatíveis com WireMock

3. Idempotência
- Repositório de idempotência baseado em Redis e um Servlet Filter para prevenir requisições duplicadas via cabeçalho `Idempotency-Key`

4. Observabilidade
- Configuração de TraceId no MDC no Orchestrator (exemplo)
- Dependências do OpenTelemetry adicionadas (configuração inicial deixada como placeholder para exportadores de produção)

5. Testes
- Teste de integração usando WireMock (F2/F3) e MockWebServiceServer (F1)
- Dependências do Testcontainers incluídas no build.gradle.kts

6. Persistência
- Getters/setters da entidade VehicleAnalysisLog implementados e o repositório armazena JSON básico da resposta

Como rodar testes locais (exemplo):

- Rode um Redis local (ou ajuste spring.redis.host/port no application.yml)
- Build: `./gradlew build`
- Execute os testes: `./gradlew test`
