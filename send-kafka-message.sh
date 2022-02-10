#!/bin/sh

# create topics
docker run --network kafka-postgres confluentinc/cp-kafka kafka-topics --create --if-not-exists --topic orders-in --bootstrap-server kafka:9092
docker run --network kafka-postgres confluentinc/cp-kafka kafka-topics --create --if-not-exists --topic orders-out --bootstrap-server kafka:9092

# send message with headers
docker run -it --network kafka-postgres confluentinc/cp-kafkacat /bin/bash -c \
'echo {\"status\":\"NEW\"} | kafkacat -b kafka:9092 -H id=$(uuidgen) -H type=CREATE_ORDER -t orders-in -P'
