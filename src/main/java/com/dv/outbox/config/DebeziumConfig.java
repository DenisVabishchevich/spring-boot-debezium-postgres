package com.dv.outbox.config;

import com.dv.outbox.listener.DebeziumListener;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DebeziumConfig {

    @Bean(destroyMethod = "close")
    DebeziumEngine<RecordChangeEvent<SourceRecord>> engine(io.debezium.config.Configuration customerConnector,
                                                           DebeziumListener debeziumListener) {
        DebeziumEngine<RecordChangeEvent<SourceRecord>> engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
            .using(customerConnector.asProperties())
            .notifying(debeziumListener)
            .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);
        return engine;
    }

    @Bean
    public io.debezium.config.Configuration customerConnector(DataSourceProperties dataSource) {
        URI uri = URI.create(dataSource.getUrl().substring(5));
        return io.debezium.config.Configuration.create()
            .with("name", "orders-postgres-connector")
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.KafkaOffsetBackingStore")
            .with("offset.storage.topic", "debezium-offset")
            .with("offset.storage.partitions", "1")
            .with("offset.storage.replication.factor", "1")
            .with("bootstrap.servers", "http://localhost:29092")
            .with("offset.commit.policy", "io.debezium.engine.spi.OffsetCommitPolicy$PeriodicCommitOffsetPolicy")
            .with("offset.flush.interval.ms", 60000)
            .with("offset.flush.timeout.ms", 5000)
            .with("internal.key.converter", "org.apache.kafka.connect.json.JsonConverter")
            .with("internal.value.converter", "org.apache.kafka.connect.json.JsonConverter")
            .with("database.dbname", uri.getPath().split("/")[1])
            .with("database.server.name", uri.getHost() + "-" + uri.getPath().split("/")[1])
            .with("database.hostname", uri.getHost())
            .with("database.port", uri.getPort())
            .with("database.user", dataSource.getUsername())
            .with("database.password", dataSource.getPassword())
            .with("plugin.name", "pgoutput")
            .with("table.include.list", "public.orders_outbox")
            .build();
    }
}
