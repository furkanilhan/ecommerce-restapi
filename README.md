# E-commerce Project

This project is a Spring Boot application that includes the basic functionalities of an e-commerce platform. Below are detailed instructions for setting up and configuring the project.

## Table of Contents

- [Getting Started](#getting-started)
- [Setup and Running](#setup-and-running)
- [Using Docker](#using-docker)
- [Manual Setup with Maven](#using-maven)
- [Dependencies](#dependencies)
- [Database Schema](#database-schema)
- [API Usage](#api-usage)

## Getting Started

This project is developed using Spring Boot 3.3.1, Spring Security 6.3.1, and Java 17. PostgreSQL is used as the database, and Liquibase is used for configuration management.

### Requirements

- Java 17
- Maven
- Docker
- Docker Compose

## Setup and Running

### Using Docker

1. Make sure **Docker and Docker Compose** are installed.

2. Open a terminal in the project directory and run the following command:
   ```sh
   docker-compose up --build -d
   ```
### Manual Setup with Maven

1. Ensure that the PostgreSQL database is running. (You can use the command `docker-compose up --build -d postgresql`.)

2. **Open a terminal in the project directory and run the following command:**

   ```sh
   mvn clean install
   ```
3. **Once the application is running, you can access it at http://localhost:8080/api.**


## Dependencies

The project includes the following dependencies:

- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web
- Spring Boot Starter Validation
- PostgreSQL Driver
- Liquibase Core
- Lombok
- JJWT
- MapStruct
- Stripe Java SDK
- H2 Database (for test)

## Database Schema

The project database schema is managed with Liquibase. Changes to the schema are defined in the `src/main/resources/db/changelog/changelog.xml` file.

## API Usage

You can use the app by importing the JSON file shared in `postman/postman_collection.json` into your Postman app.

First, sign up for the Ecommerce app. After signing in, get the token. Copy the token and go to the 
`Environments > Globals` section in the Postman app. Create a global variable with the name `furkanToken` and paste 
the token as the value. After that, you can use the other methods.

## License

This project does not have an open-source license. For any information regarding the license, please contact [me](mailto:furkan7481@gmail.com)
