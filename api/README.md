api
===

## Development

### Set-Up

To start developing, you need to install [onpenjdk-17](https://openjdk.java.net/projects/jdk/17/) and an IDE, IntelliJ preferred.

### Run local from IDE

If you want to run the API server locally, just hit run from the IDE (ApiApplication).
For local development [h2 in memory](https://www.h2database.com/html/main.html) is used as a database.

### Run local from docker

If you want to run API locally by using docker, just run `docker-compose build && docker-compose up -d`

## Documentation

### OpenAPI

OpenAPI documentation is available at [/doc/swagger/](http://localhost:8080/doc/swagger) and the json at [/doc/openapi.json](http://localhost:8080/doc/openapi.json).
