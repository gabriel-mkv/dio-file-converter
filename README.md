# File Converter

Bem-vindo ao projeto **File Converter**. Esta aplicação foi desenvolvida como um projeto prático para o **Bootcamp Accenture da DIO**, com o objetivo principal de consolidar o conhecimento em **Padrões de Projeto (Design Patterns)** aplicados ao ecossistema Java e Spring Boot.

## 🎯 Objetivo

O sistema tem como finalidade processar arquivos de transações financeiras, convertendo dados brutos em registros estruturados em banco de dados, garantindo integridade e padronização.

## 🏗️ Padrões de Projeto Utilizados

Este projeto serviu como laboratório para a implementação prática dos seguintes padrões:

*   **Singleton**: Utilizado através do container de Injeção de Dependência do Spring, garantindo que serviços e componentes tenham instância única gerenciada.
*   **Strategy**: Aplicado para permitir o processamento de diferentes formatos de arquivo (ex: PDF, CSV) sem alterar a lógica de negócio principal, encapsulando os algoritmos de parsing.
*   **Factory Method**: Utilizado para instanciar o "Parser" correto dinamicamente com base no tipo de arquivo recebido.
*   **Repository**: Abstração da camada de persistência, separando a lógica de acesso a dados da lógica de negócio (via Spring Data JPA).
*   **Decorator**: Observado no uso de anotações como `@JsonFormat` e `@JsonPropertyOrder` para modificar o comportamento de serialização dos objetos sem alterar sua estrutura de classe.

## 🚀 Tecnologias

*   **Java 21**
*   **Spring Boot 4**
*   **Spring Data JPA** (Hibernate)
*   **H2 Database** (Banco em memória para desenvolvimento)
*   **Jackson** (Processamento JSON e CSV)
*   **OpenPDF** (Geração de arquivos PDF)
*   **SpringDoc OpenAPI** (Documentação com Swagger)
*   **Maven**

## 🗄️ Modelo de Dados

A entidade principal do sistema é a `Transaction`, mapeada para a tabela `transactions`:

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | Long | Identificador único (Auto Increment) |
| `transaction_date` | Date | Data da ocorrência |
| `description` | String | Descrição da transação |
| `value` | BigDecimal | Valor monetário (precisão 10,2) |
| `category` | String | Categoria da despesa/receita |

## ⚙️ Como Executar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/gabrielmkv/file-converter.git
   ```

2. **Compile o projeto:**
   ```bash
   ./mvnw clean install
   ```
   > **Nota:** No Windows, utilize `mvnw clean install`.

3. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```
   > **Nota:** No Windows, utilize `mvnw spring-boot:run`.

4. **Acesse:**
   A aplicação iniciará (por padrão) na porta `8080`.
   *   **Documentação API (Swagger):** http://localhost:8080/swagger

## 🗃️ Interagindo com a Aplicação

Como o projeto utiliza um banco em memória (H2), os dados são resetados a cada reinicialização. Para popular o banco e testar a geração de relatórios:

### 1. Via arquivo SQL (Automático)
Substitua o arquivo `transactions-data.sql` na pasta `src/main/resources` com as inserções desejadas (o nome do arquivo deve ser mantido). O Spring Boot executará os comandos automaticamente na inicialização.

**Exemplo de estrutura:**
```sql
INSERT INTO transactions (transaction_date, description, value_brl, category) VALUES 
('2023-10-01', 'Salário', 5000.00, 'Receita'),
('2023-10-05', 'Aluguel', -1500.00, 'Moradia'),
('2023-10-10', 'Supermercado', -450.50, 'Alimentação');
```

### 2. Via H2 Console
Acesse `http://localhost:8080/h2-console` para gerenciar o banco de dados.
*   **JDBC URL:** `jdbc:h2:mem:testdb`
*   **User Name:** `sa`
*   **Password:** `password`

> **Atenção:** Ao gerenciar o banco de dados, certifique-se de manter a estrutura da tabela compatível com a definição da classe `Transaction.java`. A tabela `transactions` deve conter as colunas: `id` (auto-incremento), `transaction_date`, `description`, `value_brl` e `category`.

---

## 👨‍💻 Autor

Desenvolvido por **Gabriel** ([gabriel-mkv](https://github.com/gabriel-mkv)).
