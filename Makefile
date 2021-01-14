dev:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

prod:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

test:
	./mvnw test -Dspring.profiles.active=


compose-build:
	docker system prune -a --volumes
	mvn clean install
	docker-compose -f docker/dev/docker-compose.yml up

compose:
	docker-compose -f docker/dev/docker-compose.yml up

decompose:
	docker-compose -f docker/dev/docker-compose.yml down

