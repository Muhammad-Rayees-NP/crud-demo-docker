CRUD Demo with Spring Boot and Docker
Project Overview
This project is a simple CRUD (Create, Read, Update, Delete) application built using Spring Boot and containerized with Docker. It provides API endpoints to manage users with pagination and authentication. Additionally, Prometheus and Grafana are integrated for monitoring and visualization of the application's performance.

Technologies Used
Spring Boot (REST API)
Spring Data JPA (Database ORM)
Spring Security (Authentication & Password Encryption)
H2 / MySQL (Database)
Docker (Containerization)
Prometheus (Monitoring)
Grafana (Visualization)
JUnit & Mockito (Unit Testing)
Swagger (OpenAPI) (API Documentation)
Setup & Installation
1. Clone the Repository
Clone the repository to your local machine:

bash
Copy
Edit
git clone https://github.com/Muhammad-Rayees-NP/crud-demo-docker.git
2. Build & Run the Application
Follow these steps to build and run the application:

Clean and Install Dependencies

bash
Copy
Edit
mvn clean install
Package the Application

bash
Copy
Edit
mvn clean package
Docker Setup

First, bring down any existing containers:

bash
Copy
Edit
docker compose down
Build the Docker images:

bash
Copy
Edit
docker compose build
Finally, bring up the containers:

bash
Copy
Edit
docker compose up
Your application, along with Prometheus and Grafana, will be up and running in Docker containers.

Prometheus & Grafana Setup
Once the application is running, you can access the following monitoring tools:

Prometheus: Access the Prometheus dashboard at http://localhost:9090/.
Prometheus is used to collect and query metrics from the application.
Grafana: Access the Grafana dashboard at http://localhost:3000/.
Grafana is used to visualize the metrics collected by Prometheus.
The default login for Grafana is:
Username: admin
Password: admin (you will be prompted to change it after logging in)
Running Tests
To run the unit tests using JUnit and Mockito:

bash
Copy
Edit
mvn test
API Documentation
The API is documented using Swagger (OpenAPI). Once the application is running, you can access the documentation by navigating to:

bash
Copy
Edit
http://localhost:8080/swagger-ui.html
Notes
This project supports pagination for the user management API.
Spring Security is implemented for authentication, and password encryption is done using BCrypt.
You can choose between H2 (for in-memory database) or MySQL (for persistent storage) as your database.
Prometheus and Grafana are configured to monitor and visualize application metrics, including system resource usage and application-specific data.
Let me know if you need any more changes!
