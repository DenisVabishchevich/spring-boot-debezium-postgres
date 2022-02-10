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
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage.file.filename", "/tmp/offsets.dat")
            .with("offset.flush.interval.ms", 60000)
            .with("name", "orders-postgres-connector")
            .with("database.server.name", uri.getHost() + "-test")
            .with("database.hostname", uri.getHost())
            .with("database.port", uri.getPort())
            .with("database.user", dataSource.getUsername())
            .with("database.password", dataSource.getPassword())
            .with("database.dbname", "test")
            .with("plugin.name", "pgoutput")
            .with("table.whitelist", "public.orders_outbox")
            .build();
    }
}
