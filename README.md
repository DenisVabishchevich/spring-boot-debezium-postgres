![coverage](.github/badges/jacoco.svg)
![branches coverage](.github/badges/branches.svg)
![pipeline status](https://github.com/DenisVabishchevich/spring-boot-debezium-postgres-kafka/actions/workflows/build.yaml/badge.svg)
## Transaction outbox pattern implementation

### Prerequisites

1. Java 17
2. Docker

### How to build and test

1. `docker-compose up -d`
2. `./gradlew clean build`
3. `docker-compose stop`

### How to run

1. Setup postgres and kafka
    * `docker-compose up -d`
2. Create topics and send message to kafka
    * `./send-kafka-message.sh`
3. Run spring boot application
    * `./gradlew bootRun`
4. Monitor kafka orders out topic
   * `./recieve-kakfka-messages.sh`
5. Shutdown docker compose
   * `docker-compose stop`