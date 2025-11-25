# 🎬 Screenmatch Spring

![Java](https://img.shields.io/badge/Java-17+-red?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-Build-blue?logo=apachemaven)
![Status](https://img.shields.io/badge/Status-En%20Desarrollo-yellow)

Aplicación desarrollada en **Java + Spring Boot**, basada en el contenido de los cursos de Alura LATAM.  
El proyecto permite consumir datos de películas/series, procesarlos, almacenarlos y exponer servicios REST.  
Incluye integración opcional con **OpenAI API**.

---

## 🚀 Tecnologías utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Maven
- H2 o MySQL (según configuración)
- OpenAI API (opcional)

---

## 📦 Funcionalidades principales

- Consumo de APIs externas para obtener información de películas y series.
- Persistencia de datos mediante JPA.
- Exposición de endpoints REST.
- Lógica de negocio organizada en servicios.
- Integración opcional con OpenAI para análisis o generación de texto.
- Manejo seguro de claves mediante variables de entorno.

---

## 📁 Estructura del proyecto

src/
└── main/
├── java/com/aluracursos/screenmatch/
│ ├── controller/ -> Controladores REST
│ ├── model/ -> Entidades
│ ├── repository/ -> Repositorios JPA
│ └── service/ -> Lógica de negocio
└── resources/
├── application.properties
└── static / templates (si aplica)


---

## 🔧 Configuración del proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/TnlComputer/screenmatch-spring.git
cd screenmatch-spring

### 2️⃣ Configurar variables de entorno

Windows (PowerShell)
setx OPENAI_API_KEY "tu_api_key"

Linux / macOS
export OPENAI_API_KEY="tu_api_key"

### 3️⃣ Ejecutar la aplicación

mvn spring-boot:run

### 🧪 Endpoints de ejemplo

| Método | Endpoint           | Descripción                    |
| ------ | ------------------ | ------------------------------ |
| GET    | `/peliculas`       | Lista todas las películas      |
| POST   | `/peliculas`       | Agrega una película nueva      |
| GET    | `/series`          | Lista series                   |
| GET    | `/openai/analizar` | (Opcional) Análisis con OpenAI |

🛡️ Seguridad

Este repositorio no debe contener claves API ni datos sensibles.
Utilizar siempre variables de entorno para configuraciones privadas.

🧑‍💻 Autor

Jorge Gustavo Martinez
Analista Programador — Java, Laravel, Python, BI
GitHub: https://github.com/TnlComputer