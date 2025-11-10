package com.gestaoestabelecimentos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTest.class);
    private final DataSource dataSource;

    public DatabaseConnectionTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("✅ CONEXÃO COM ORACLE FIAP BEM-SUCEDIDA!");
            logger.info("✅ Database: {}", connection.getMetaData().getDatabaseProductName());
            logger.info("✅ URL: {}", connection.getMetaData().getURL());
            logger.info("✅ Usuário: {}", connection.getMetaData().getUserName());
        } catch (Exception e) {
            logger.error("❌ ERRO NA CONEXÃO COM ORACLE: {}", e.getMessage());
        }
    }
}