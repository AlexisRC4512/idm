Proyecto de Microservicios - Orders & Products

Este proyecto contiene el  microservicio desarrollado con Spring Boot 3, WebFlux, programación reactiva y programación funcional con Optionals. el servicio gestionan Órdenes

⸻

🏗 Estructura del Proyecto

1. Microservicio de Órdenes
	•	Puerto: 8080
	•	Responsabilidades:
	•	Crear, listar y gestionar órdenes.
	•	Cada orden contiene un producto que se obtiene del microservicio de Productos.

URL de Swagger UI:
http://localhost:8080/swagger-ui/index.html#

⸻

2. Microservicio de Productos
	•	Puerto: 8082
	•	Responsabilidades:
	•	Crear, listar, actualizar, buscar por id y eliminar productos.
	•	Los productos son utilizados por el microservicio de Órdenes.

URL de Swagger UI:
http://localhost:8082/swagger-ui/index.html#

⸻

⚡ Cómo Ejecutar los Microservicios
	1.	Construir el proyecto

./mvnw clean install

	2.	Ejecutar el microservicio de Órdenes

./mvnw spring-boot:run

	•	Se ejecuta en http://localhost:8080

	3.	Ejecutar el microservicio de Productos

./mvnw spring-boot:run

	•	Se ejecuta en http://localhost:8082

⸻

🔹 Ejemplos de Peticiones

Órdenes
	•	GET todas las órdenes:

GET http://localhost:8080/api/orders

	•	POST crear orden:

POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "name": "Orden de Laptop",
  "productId": "1",
  "description": "Orden para Dell XPS",
  "typeOrder": "ONLINE",
  "quantity": 2
}
