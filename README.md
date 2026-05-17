# TaskManager API

Uma API REST para gerenciamento de tarefas pessoais com autenticação segura por JWT.

## 🚀 Deploy

A API está disponível em produção:https://taskmanager-production-6031.up.railway.app/

Documentação interativa (Swagger):https://taskmanager-production-6031.up.railway.app/swagger-ui/index.html

---

## 📋 Funcionalidades

- ✅ Autenticação e registro de usuários com JWT
- ✅ CRUD completo de tarefas
- ✅ Filtro de tarefas por status
- ✅ Busca de tarefas por palavra-chave
- ✅ Tratamento global de exceções
- ✅ Documentação interativa via Swagger/OpenAPI

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.14 |
| Spring Security | 6 |
| Spring Data JPA | - |
| PostgreSQL | - |
| JWT (jjwt) | 0.12.3 |
| Springdoc OpenAPI | 2.8.8 |
| Lombok | - |
| Maven | - |

---

## 🔐 Autenticação

**1. Registrar usuário:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nome": "Camille Fernandes",
  "email": "camille@email.com",
  "senha": "sua_senha"
}
```

**2. Fazer login e obter token:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "camille@email.com",
  "senha": "sua_senha"
}
```

**3. Usar o token nas requisições:**
```http
Authorization: Bearer {seu_token_jwt}
```

---

## 📌 Endpoints

### Autenticação
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Registrar novo usuário |
| POST | `/api/auth/login` | Autenticar e obter token JWT |

### Tarefas
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/tarefas` | Listar todas as tarefas |
| GET | `/api/tarefas/{id}` | Buscar tarefa por ID |
| GET | `/api/tarefas/status/{status}` | Filtrar tarefas por status |
| GET | `/api/tarefas/pesquisa` | Buscar tarefas por palavra-chave |
| POST | `/api/tarefas` | Criar nova tarefa |
| PUT | `/api/tarefas/{id}` | Atualizar tarefa |
| DELETE | `/api/tarefas/{id}` | Excluir tarefa |

---

## ⚙️ Como rodar localmente

**1. Clonar o repositório:**
```bash
git clone https://github.com/CamilleFernandes/taskmanager.git
cd taskmanager
```

**2. Rodar a aplicação:**
```bash
./mvnw spring-boot:run
```

**3. Acessar o Swagger:**http://localhost:8080/swagger-ui/index.html

---

## 👩‍💻 Autora

**Camille Fernandes**
Estudante de Engenharia de Software — Anhanguera
Belo Horizonte, Minas Gerais, Brasil

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/camillefernandes-21010a406)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/CamilleFernandes)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:camillelopesdsf@gmail.com)

---

## 📄 Licença

Este projeto está sob a licença MIT.