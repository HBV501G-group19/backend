## Requirements

* [Postgresql v10.10](https://postgresql.org/)
* [Postgis 2.4](https://postgis.net/)
* An API key for the [Open Route Service](https://openrouteservice.org/dev)

## Database
Before running the project setting up a database running Postgis is necessary. This can be done by running the command
```sql
CREATE EXTENSION POSTGIS;
```
in your database, after Postgis has been set up on your computer.

## Application properties
An example can be found in [application.properties.example](./tree/src/main/resources/application.properties.example)
The properties that must be changed to reflect your system are
* `spring.datasource.url`
* `spring.datasource.username`
* `spring.datasource.password`
* `ors.key`
* `jwt_secret`

## Building and running
The project comes prepackaged in a Maven wrapper. It can be run with

```bash
./mvnw install && ./mvnw spring-boot:run
```

It can also be seen running at https://ancient-wave-00930.herokuapp.com/ . Note that it is a REST API and requires a bearer token with every request for authentication. It is not possible to access the endpoints directly through a browser, so we recommend a REST client like Postman or Curl to communitcate.

Documentation for the API can be found [here](https://github.com/HBV501G-group19/docs-and-files) 
