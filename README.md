## Spring Boot Kotlin Template

This is a Spring Boot template project using Kotlin, Spring Boot 3, Postgres, Flyway, OAuth2, Jwt, ArrowKt, Kotest, and
MockK.

## Requirements

1. Java 11 or higher
2. Gradle 6.8 or higher
3. Kotlin 1.5.21 or higher
4. Postgres 13.3 or higher

**Running the App**
Type the following command in your terminal to run the app:

     ./gradlew bootRun

**Build and Run with Docker**
Build the project with gradle:

    ./gradlew build

Build Docker and run docker with docker-compose:

    cd ./docker

    docker build -t app

    docker-compose up -d

The app will start running at  [http://localhost:8080](http://localhost:8080/)

## Rest APIs

The app defines following for APIs.

    GET /api/v1/user
    {
        "code": 200,
        "data": {
            "id": 1,
            "username": "Test",
            "name": "ts",
            "email": "test@gmail.com",
            "created_at": 1700031810575
        }
    }

    GET /api/v1/user/1
    {
        "code": 200,
        "data": {
            "id": 1,
            "username": "Test",
            "name": "ts",
            "email": "test@gmail.com",
            "created_at": 1700031810575
        }
    }

    POST /api/v1/user
    {
	    "name":"Walter W",
	    "username":"saymyname"
    }

## Features

The project uses a [jOOQ extension plugin](https://github.com/vendelieu/jooq-extension) that extends DAO with interface
that have some helpful functions which often covers the need for repositories, so don't be surprised.

Project also uses a Kotlin idiomatic approach in the error handling process - “exceptions for exceptional cases”, i.e.
business logic and its failures are processed manually and are completely covered without throwing exceptions for every
sneeze and smearing logic out of context.
