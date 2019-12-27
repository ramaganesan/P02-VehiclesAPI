# ND035-P02-VehiclesAPI-Project

Project repository for JavaND Project 2, where students implement a Vehicles API using Java and Spring Boot that can communicate with separate location and pricing services.

## Instructions

Check each component to see its details and instructions. Note that all three applications
should be running at once for full operation. Further instructions are available in the classroom.

- [Vehicles API](vehicles-api/README.md)
- [Pricing Service](pricing-service/README.md)
- [Boogle Maps](boogle-maps/README.md)

## Dependencies

The project requires the use of Maven and Spring Boot, along with Java v11.
The application requires a specific start up.
First Eureka Server
Next will be the Pricing and Boogle Maps Microservice.
Please make sure they register with the Eureka Server.
Last is the VehicleAPIApplication.
java -jar target\eureka-0.0.1-SNAPSHOT.jar
java -jar target\pricing-service-0.0.1-SNAPSHOT.jar
java -jar target\boogle-maps-0.0.1-SNAPSHOT.jar
http://localhost:8761/ => Please make sure the Microservices are registered.
java -jar target\vehicles-api-0.0.1-SNAPSHOT.jar
