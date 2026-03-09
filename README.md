# LiveCoding Microservice



## Architecture finale
Client
  |
  v
API Gateway (8080)
  |
  v
USER-SERVICE (8083)
  |
  v
MySQL Docker (3307)

API-GATEWAY -----> Eureka Server (8762)
USER-SERVICE ----> Eureka Server (8762)




#### `user-service`

- `Spring Web`
- `Spring Data JPA`
- `MySQL Driver`
- `Validation`
- `Spring Boot Actuator`
- `Lombok`

à ajouter :
- `MapStruct`
- `mapstruct-processor`
- `lombok-mapstruct-binding`

Aprés,  `user-service` on ajout ça pour Eureka :
- `spring-cloud-starter-netflix-eureka-client`
- `spring-cloud.version`
- `dependencyManagement` 


#### `discovery-service`
- `Eureka Server`
- `Spring Boot Actuator`




#### `api-gateway`
Dans Initializr, tu prends :
- `Gateway`
- `Eureka Discovery Client`
- `Spring Boot Actuator`


## Etape 0. Base de donnees avec Docker

Avant de lancer le CRUD, il faut une base.


```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: mysql-user-service
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: user_db
      MYSQL_USER: user
      MYSQL_PASSWORD: user123
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  phpmyadmin:
    image: phpmyadmin:latest
    container_name: phpmyadmin-user-service
    restart: unless-stopped
    depends_on:
      - mysql
    environment:
      PMA_HOST: mysql
      PMA_PORT: 3306
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "8082:80"

volumes:
  mysql_data:
```



## Etape 1. Creer `user-service` seul

### `application.yaml` au debut
```yaml
spring:
  application:
    name: user-service

  datasource:
    url: jdbc:mysql://localhost:3307/user_db?createDatabaseIfNotExist=true&serverTimezone=UTC
    username: user
    password: user123
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8083

management:
  endpoints:
    web:
      exposure:
        include: health,info
```



## Etape 2. Lier `user-service` a Eureka

### Ce qu'on ajoute dans `pom.xml`
Ajouter dans `properties` :

```xml
Dans <properties>

<spring-cloud.version>2025.0.1</spring-cloud.version>
```

aprés on ajoute :

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

enfin dans `dependencies` :

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### Ce qu'on ajoute dans `application.yaml`
Ajouter :

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka
```



## Etape 3. Creer `discovery-service`

### Dependances Initializr
- `Eureka Server`
- `Spring Boot Actuator`

### `application.yaml`
```yaml
server:
  port: 8762

spring:
  application:
    name: discovery-service

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### Classe principale
Ajouter `@EnableEurekaServer`.

## Etape 4. Creer `api-gateway`


### Dependances Initializr
- `Gateway`
- `Eureka Discovery Client`
- `Spring Boot Actuator`


### `application.yaml`
```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      mvc:
        routes:
          - id: user-service
            uri: lb://USER-SERVICE
            predicates:
              - Path=/users/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka

management:
  endpoints:
    web:
      exposure:
        include: health,info
```



docker compose up -d

mvn spring-boot:run

