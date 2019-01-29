# Netflix'n Chill

This project is a series manager web page, where users can select which series they have watched before
(even by seasons or episodes), what is their favourites and or what they would like to watch. From this information the user
has given, they can see on their profile page some statistics about themselfes, for example, how much time did they 'waste'
watching different series. There is also a search feature where not registered users may look up certain series if they
would liek to.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

What things you need to install the software and how to install them:
- your favourite IDE
- Angular 6+ for the frontend to work properly
- Spring Boot for the backend -> add environment variables as shown in `application.properties` (linked below)
- `node_modules` and `e2e` libraries added to frontend package (Angular generates these at a new project start)
- also run `npm install` to load all packages used in the project

```
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
```

### Running the project

A step by step series of examples that tell you how to get a development env running

1. Go to the `backend/src/main/java/com.codecool.netflixandchill` package and run `ApplicationStart.java`

2. Go to the `frontend` module and run `ng serve`

3. Go to `localhost:4200` and enjoy the page and it's features

## Built With

* [Angular 6](https://angular.io/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring](https://spring.io/) - Server side framework


## Security and Publicity

The page uses Spring Security to handle users correctly, since this is a REST application. Json Web Token is sent to the user
back when logged in to be able to recognize the exact user when he requests some resource from the server later.

**Important to note:** the server's URL-s starting with `/series/` are public so anyone can reach the information stored there, you don't have to be logged in or to be registered.
