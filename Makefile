LOAD_ENV = set -a && . ./.env && set +a

migrate:
	$(LOAD_ENV) && ./mvnw flyway:migrate

start-server:
	$(LOAD_ENV) && ./mvnw clean spring-boot:run

check-style:
	./mvnw checkstyle:check

compile:
	./mvnw compile
