# Executando a API Localmente

Este guia descreve como executar a API localmente e como realizar uma chamada HTTP para ela.

## Pré-requisitos

* **Java 21:** Certifique-se de ter o Java 21 instalado.
* **Gradle:** Certifique-se de ter o Gradle instalado.
* **Docker:** Certifique-se de ter o Docker instalado e em execução.

## Executando a API

1. **Clone o Repositório:**
   Clone o repositório do projeto para sua máquina local.

2. **Construindo o Projeto:**
   Navegue até o diretório raiz do projeto e execute o seguinte comando para construir o projeto:

   ```bash
   ./gradlew build
   ```

3. **Executando com Docker Compose:**
   Para executar a API e o WireMock (servidor de simulação de API externa) usando Docker Compose, execute o seguinte
   comando no diretório raiz do projeto:

   ```bash
   docker-compose -f local-integration-tests/docker-compose.yml up
   ```

   Este comando irá iniciar o WireMock na porta 8383 e a API Spring Boot na porta 8181.

## Entendendo o WireMock

O WireMock é usado para simular a API externa de clima. Um arquivo de mapeamento (`mappings/weather-stub.json`) define a
resposta que o WireMock retornará para uma requisição específica.

**Conteúdo do `mappings/weather-stub.json`:**

```json
{
  "request": {
    "method": "GET",
    "urlPath": "/weathers",
    "queryParameters": {
      "zipCode": {
        "equalTo": "37757000"
      }
    }
  },
  "response": {
    "status": 200,
    "body": "{\"temperature\": 25, \"condition\": \"Sunny\"}",
    "headers": {
      "Content-Type": "application/json"
    }
  }
}
```

Este mapeamento responde a requisições GET para `/weathers` com o parâmetro de consulta `zipCode` igual a `37757000` com
um código de status 200 e um corpo JSON contendo `temperature: 25` e `condition: Sunny`.

## Realizando uma Chamada HTTP

A API está disponível na porta 8181. Para obter informações sobre o clima para um determinado código postal, você pode
fazer uma requisição GET para o seguinte endpoint:

`http://localhost:8181/weathers?zipCode=<código_postal>`

**Exemplo:**

Para obter informações sobre o clima para o código postal `37757000`, você pode fazer a seguinte requisição:

`http://localhost:8181/weathers?zipCode=37757000`

A resposta será um JSON no seguinte formato:

```json
{
  "condition": "Sunny",
  "temperature": "25 ºC"
}
```

## Parando a API

Para parar a API e o WireMock, execute o seguinte comando no diretório raiz do projeto:

```bash
docker-compose -f local-integration-tests/docker-compose.yml down
```

## Observações

* O código postal `37757000` é usado no mapeamento do WireMock. Se você usar um código postal diferente, o WireMock
  retornará um erro 404, pois não há um mapeamento correspondente.
* A API Spring Boot está configurada para usar `http://localhost:8383` como base URL para as requisições externas, que é
  o endereço do WireMock.
* O arquivo `application.yaml` configura a porta da aplicação Spring Boot para 8181.