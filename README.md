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


## Etape 1. Creer `user-service` seul

## Etape 2. Lier `user-service` a Eureka

#### `user-service`

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

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


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




