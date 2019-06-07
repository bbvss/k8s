## Setup Prerequisites

This project shows a simple Spring Boot application. The application will be deployed to a Kubernetes cluster.

The Kubernetes cluster is created via docker-for-desktop, which provides tools for creating clusters on a local machine easily. Keycloak, the database, and the Spring Boot application are deployed to the newly created cluster. 

The following versions are used throughout the example:

* __Docker_Engine__: 18.09.02
* __Kubernetes__: v1.10.11 
* __Spring Boot__: 2.1.5.RELEASE
* __OpendJDK__: 12