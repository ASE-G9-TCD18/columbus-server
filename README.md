# Columbus Server Application

Columbus is a Journey Sharing App that allows users with similar interests to connect with other users who share a common destination. Users in a shared journey can collaborate with each other to set meeting points, mode of transport and book a taxi. All decisions in a group are made by majority voting that allows distribution of control. Users may set preferences for a journey such as M/F, user rating, etc.


Columbus allows two types of trips:

* Scheduled Trips
* Immediate Trips
 
## Running Development Server

mvn spring-boot:run

## Maven build, and test
mvn clean package

## Postman Request collection:
https://www.getpostman.com/collections/61cc634a6b549795cf42

## Test Properties
spring.data.mongodb.port=0
