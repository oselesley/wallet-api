# WALLET API [![Build Status](https://ci-next.docker.com/public/buildStatus/icon?job=compose/master)](https://ci-next.docker.com/public/job/compose/job/master/)
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
    
    
 



API Documentation Link: 
Drop API LINK HERE

Technologies Used
* Intellij: 
* Spring Boot:
* Docker
* Github Actions (CI/CD)
* JUNIT
* Spring Security
* PostgreSQL 