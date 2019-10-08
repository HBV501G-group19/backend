## Requirements

* [Postgresql v10.10](https://postgresql.org/)
* [Postgis 2.4](https://postgis.net/)

## Database
Before running the project setting up a database running Postgis is necessary. This can be done by running the command
```sql
CREATE EXTENSION POSTGIS;
```
in your database.

## Application properties
An example can be found in [application.properties.example](./tree/src/main/resources/application.properties.example)

## Building and running
The project comes prepackaged in a Maven wrapper. It can be run with

```bash
./mvnw install && ./mvnw spring-boot:run
```


