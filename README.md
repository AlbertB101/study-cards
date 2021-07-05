# Study cards
REST server-side application that provides API for managing language packs with cards for client-side application for learning foreign words with flashcards.


## Project description

The project uses:

- [Spring (MVC)](http://github.com/spring-projects/spring-framework)
- [Spring Boot](http://github.com/spring-projects/spring-boot)
- [Spring Data JPA](http://github.com/spring-projects/spring-data-jpa)
- [Spring Security](http://github.com/spring-projects/spring-security)
- [Hibernate ORM](https://github.com/hibernate/hibernate-orm)
- [Project Lombok](https://github.com/projectlombok/lombok)
- [JUnit 5](https://github.com/junit-team/junit5)
- [Mockito ](https://github.com/mockito/mockito)
- [JSON Web Token](https://github.com/mockito/mockito)

Also the project saves data in PostgreSql database.


The project consists of mainly two parts: Authorization server and Resource server. 
- Authrization server provides API for login and returns jwt token for accessing Resource server. But Authorization server temporarily provieds API for authentication (registrationg new users). It allows to register new users and to manage their accounts. 
- Resource server contains domain model and provides API for managing LangPacks (language pack that contains user created cards) and Cards (flash card that is used by client to learnig new foreign words).API allows to create, receive, update and delete LangPacks and Cards.

## Rest
- The project doesn't have client-server state and doesn't hold any information about users/clients or their requests.
- Communication with controllers is carried out by exchanging Json files.


## Database
It uses a PostgreSQL database, can be changed easily in the `application.properties` for any other database.


## Security
Every access to endpoints (except registration and login endpoints) should be authorized. Every registered user gets JWT token if login request was successful satisfied. Authorization supports various roles of user i.e. some endpoints may be accessed only by someone who has "developer" authorities. 

### Request processing flow
Most requests are handled in a similar way: 
1. Authorization. If authorization is successful, the data from the http request is passed on to the controller;
2. Controller. Controller delegates requst processing to service layer;
3. Service. Service processes the request and delegates data saving to Repositories; 
4. Repository. Repositories update data by making requests to database.


