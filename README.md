# Business-Allocation
* [General info](#general-info)
* [Technologies](#technologies)
* [MVP](#mvp)
* [TODO](#TODO)

## General info
Business allocation is an application for managing both the company and the employees and users providing services in our company. The application allows you to set up an account by verifying your e-mail address and is based on JWT and OAuth 2.0 technology.
The flagship functionality of the application is managing and assigning tasks to individual employees. Thanks to this solution, the head of the company or the project manager can monitor the activities of a given employee and, based on information about his skills, assign him new tasks.
Also thanks to the use of the Apache Kafka broker, it allows you to send messages within the company's internal structure. (Note Application in development).

## Technologies
The project is implemented using:
### Back-End
* Java
* Spring-Boot
* Spring-Security
* Spring-Data-JPA
* Spring-Web
* JWT
* ModelMapper
* Apache Kafka
#### Tests
* JUnit
* Mockito
### Data Base
* PostgreSQL
### Build Tools
* Docker

## TODO
* Add task management functionality
* Add employee availability
* Write tests for a given functionality
* Add e-mail verifer
* Add task review for boss or manager
* Add OAuth 2.0 for Facebook, Google and Linkedin
* Add massage broker
