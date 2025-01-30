CRUD Demo with Spring Boot and Docker

 Project Overview

This project is a simple CRUD (Create, Read, Update, Delete) application built using Spring Boot and containerized with Docker. It provides API endpoints to manage users with pagination and authentication.

Technologies Used

Spring Boot (REST API)

Spring Data JPA (Database ORM)

Spring Security (Authentication & Password Encryption)

H2 / MySQL (Database)

Docker (Containerization)

JUnit & Mockito (Unit Testing)

Swagger (OpenAPI) (API Documentation)

Setup & Installation

1. Clone the Repository

[git clone https://github.com/yourusername/crud-demo-docker.git
 cd crud-demo-docker](https://github.com/Muhammad-Rayees-NP/crud-demo-docker.git

2. Build & Run the Application

 mvn clean install
 mvn clean package
 docker compose down
 docker compose build
 docker compose up


🛠 Running Tests

Run unit tests using:

 mvn test

