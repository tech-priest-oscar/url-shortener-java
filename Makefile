migrate:
	set -a && . ./.env && set +a && ./mvnw flyway:migrate

start-server:
	set -a && . ./.env && set +a && ./mvnw clean spring-boot:run

check-style:
	./mvnw checkstyle:check
