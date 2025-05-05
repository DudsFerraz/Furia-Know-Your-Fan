# Know Your Fan - Backend

Este é o backend da aplicação **Know Your Fan**, criada para a organização de eSports **FURIA**, com o objetivo de coletar e analisar informações dos seus fãs de forma gamificada.

Este repositório contém a API (backend) do projeto. A interface web (frontend) está disponível [neste repositório](https://github.com/DudsFerraz/Furia-Know-Your-fan-frontend).

## 🌟 Visão Geral

A aplicação incentiva os usuários a interagirem com a plataforma através de:

* **Ganho de XP** para subir de nível.
* **FURIA Cash**, uma moeda virtual que pode ser trocada por brindes e produtos oficiais.

## ⚙️ Tecnologias Utilizadas

* **Java 21** com **Spring Boot**
* **PostgreSQL**
* **Google Vision API** (para OCR de documentos)
* **Twitter/X API** (para análise de perfis sociais)
* **JWT + Spring Security**
* **Docker**
* **Railway** (hospedagem)

## 🔧 Funcionalidades Principais

* Validação de CPF com classe exclusiva.
* Validação de foto do CPF via OCR com Google Vision API.
* Integração com Twitter/X para verificar se o usuário segue a FURIA e medir interações.
* Sistema de ranking por XP dos usuários.
* Sistema de loja com uso de FURIA Cash.

## 💡 Arquitetura e Qualidades do Código

* Uso de **DTOs** para proteção de dados sensíveis.
* Autenticação via **JWT**.
* Endpoints organizados em camadas: `Controllers`, `Services`, `Repositories`.
* Containerização com **Docker**.
* Configuração de **CORS** para conexão com frontend.

## 🔒 Segurança

* Configuração com `SecurityFilterChain` via `HttpSecurity`.
* `JwtAuthenticationFilter` verifica token JWT presente no cabeçalho Authorization.
* Senhas criptografadas com **BCrypt**.

## 📁 Estrutura do Projeto

```
src/
 ├── config/               # Configuração de CORS
 ├── controller/           # Endpoints REST
 ├── dto/                  # DTOs para entrada e saída
 ├── Entities/             # Entidades JPA
 ├── repository/           # Interfaces de persistência
 ├── service/              # Lógica de negócio
 └── security/             # Configuração de autenticação, JWT, etc

```

## 🛜 Endpoints REST Principais

| Método | Rota                          | Descrição                      |
| ------ | ----------------------------- | ------------------------------ |
| POST   | `/api/user/register`          | Cria novo usuário              |
| POST   | `/api/user/login`             | Autentica usuário via JWT      |
| GET    | `/api/user/{id}`              | Consulta perfil de usuário     |
| GET    | `/api/user/get/all/xp`        | Lista todos os usuários por XP |
| POST   | `/api/user/buy`               | Troca de cash por brindes      |
| POST   | `/api/user/events`            | Adiciona eventos e ganha XP    |
| POST   | `/api/user/purchases`         | Adiciona compras e ganha XP    |
| POST   | `/api/user/outsideActivities` | Atividades externas + XP       |
| POST   | `/api/document/upload`        | Valida foto de documento       |
| POST   | `/api/twitter/auth`           | Conecta usuario ao Twitter/X   |


## 🏠 Deploy

O backend é hospedado no [Railway](https://railway.app/).

Para rodar localmente:

```bash
git clone https://github.com/DudsFerraz/Furia-Know-Your-Fan-backend.git
cd know-Your-Fan-backend
docker-compose up --build
```

## 🔐 Variáveis de Ambiente

| Nome             | Descrição                        |
| ---------------- | -------------------------------- |
| `JWT_SECRET`     | Segredo para assinar o JWT       |
| `FRONTEND_URL`   | Domínio do frontend (CORS)       |
| `GOOGLE_API_KEY` | Chave da API Google Vision       |
| `TWITTER_BEARER` | Token para autenticação no X API |

## 🚀 Futuras Expansões

* Integração com outras redes sociais.
* Sistema de conquistas e badges.
* Integração com ChatBot interativo (https://github.com/DudsFerraz/FuriaBot)
