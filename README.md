# Microservicio Clientes

Este proyecto es un microservicio que proporciona funcionalidades para la gestión de clientes, incluyendo la creación, consulta y actualización de la información de los clientes, así como la gestión de sus datos de pago.

## Funcionalidades

- Creación de clientes: Permite crear un nuevo cliente proporcionando su nombre, correo electrónico, dirección y detalles de pago.
- Consulta de clientes: Permite consultar la información de un cliente existente por su ID.
- Actualización de dirección: Permite actualizar la dirección de un cliente existente.
- Actualización de detalles de pago: Permite actualizar los detalles de pago de un cliente existente.

## Tecnologías utilizadas

- Java
- Spring Boot
- Spring Data MongoDB
- JUnit 5 y Mockito (para pruebas unitarias)
- Maven (para gestión de dependencias y construcción del proyecto)

## Estructura del proyecto

El proyecto sigue una estructura de carpetas comúnmente utilizada en aplicaciones Java con Spring Boot:

- `src/main/java/`: Contiene el código fuente de la aplicación.
    - `edu.escuelaing.arcn.microservice.application`: Contiene las clases relacionadas con la capa de aplicación.
    - `edu.escuelaing.arcn.microservice.domain.model`: Contiene las clases que representan los modelos de dominio.
    - `edu.escuelaing.arcn.microservice.domain.repository`: Contiene las interfaces de repositorio para interactuar con la base de datos.
    - `edu.escuelaing.arcn.microservice.domain.service`: Contiene los servicios de negocio.
    - `edu.escuelaing.arcn.microservice.domain.controller`: Contiene los controladores REST.
- `src/test/java/`: Contiene las pruebas unitarias para la aplicación.

## Configuración

El proyecto utiliza MongoDB como base de datos. Asegúrate de tener una instancia de MongoDB en ejecución y configurada correctamente en `application.properties`.

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/clientDB
````
## Ejecución

Para ejecutar la aplicación, puedes usar Maven. Desde la raíz del proyecto, ejecuta el siguiente comando:

```bash
mvn spring-boot:run
````

## Pruebas

Se han incluido pruebas unitarias para los servicios de la aplicación. Para ejecutar las pruebas, utiliza el siguiente comando Maven:

```bash
mvn test
````

## Autores

* [Miguel Angel Salamanca](https://github.com/migueltests007)
* [Juan Camilo Bazurto](https://github.com/juan-bazurto-ec)