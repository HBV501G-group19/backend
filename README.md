## Requirements

* [Postgresql v10.10](https://postgresql.org/)
* [Postgis 2.4](https://postgis.net/)
* An API key for the [Open Route Service](https://openrouteservice.org/dev)

## Database
Before running the project setting up a database running Postgis is necessary. This can be done by running the command
```sql
CREATE EXTENSION POSTGIS;
```
in your database.

## Application properties
An example can be found in [application.properties.example](./tree/src/main/resources/application.properties.example)
The properties that must be changed are
* `spring.datasource.url`
* `spring.datasource.username`
* `spring.datasource.password`
* `ors.key`

## Building and running
The project comes prepackaged in a Maven wrapper. It can be run with

```bash
./mvnw install && ./mvnw spring-boot:run
```

## TODO:

* [ ] Finna út úr hvernig það er hægt að bæta við passenger í ride
* [ ] Setja upp endpoints fyrir user og ride
  * [ ] GET á `/ride/` og `ride/{id}` til að ná í
  * [ ] POST á `/ride` til þess að búa til nýtt
  * [ ] PATCH á `/ride/{id}` til þess að bæta við farþega og uppfærA
  * [ ] GET á `/user/` og `/user/{id}` til að ná í
  * [ ] POST á `/user` til að búa til
  * [ ] Patch á `/user` til að breyta upplýsingum(low priority)
* [ ] Setja upp search endpoint til að leita með, þýðir pottþétt þörf á controller og líklega líka service


