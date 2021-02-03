# WALLET API
![Build and Test](https://github.com/oselesley/wallet-api/workflows/Master%20Branch/badge.svg)

 An Api that allows users save and  money in multiple currencies.
 
 USER ROLES: 
 - Noobs: can only save and withdraw in one specified main currency
 - Elites:  can save money in multiple currencies;
 
 
 RUN APPLICATION:
    
   To startup the application simple run
    
    make compose 
    
   Make sure make is installed. For Mac users run:
    
    brew install make
    
   Alternatively, (mac and windows) run mvn clean install:
        
     mvn clean install
   then run
   
     docker-compose -f docker/dev/docker-compose.yml up
     
   Swagger Route
   
     http://localhost:8082/api/v1/swagger/swagger-ui-custom.html
    
    
 


[Heroku Link:](https://decagon-wallet-api.herokuapp.com/api/v1/swagger/swagger-ui-custom.html)

[API Documentation Link:](https://drive.google.com/file/d/13ChLu77hxg16cNmPSJWdy_o-TUrEyK50/view?usp=sharing)

Technologies Used
* Intellij: 
* Spring Boot:
* Docker
* Github Actions (CI/CD)
* JUNIT
* Spring Security
* PostgreSQL 
