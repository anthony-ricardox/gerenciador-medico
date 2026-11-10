# gerenciador-medico

## Executar localmente

A aplicação usa MySQL. Crie o banco `gerenciador_medico`, configure as variáveis de ambiente e execute:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/gerenciador_medico"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"
.\mvnw.cmd spring-boot:run
```

Os testes usam um banco H2 em memória e não precisam de um servidor MySQL.

## API de especialidades

Os endpoints ficam disponíveis em `/api/especialidades`:

| Método | Caminho | Resultado |
|---|---|---|
| `POST` | `/api/especialidades` | Cria uma especialidade (`201 Created`) |
| `GET` | `/api/especialidades` | Lista especialidades em ordem alfabética |
| `GET` | `/api/especialidades/{id}` | Busca uma especialidade |
| `PUT` | `/api/especialidades/{id}` | Atualiza uma especialidade |
| `DELETE` | `/api/especialidades/{id}` | Exclui uma especialidade (`204 No Content`) |

O corpo de criação e atualização aceita `nome` (obrigatório, até 100 caracteres) e
`descricao` (opcional, até 255 caracteres). Erros de validação retornam `400`,
nomes duplicados retornam `409` e IDs inexistentes retornam `404`. A documentação
OpenAPI está disponível em `/swagger-ui/index.html`.