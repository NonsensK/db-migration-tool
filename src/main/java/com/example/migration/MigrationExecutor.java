package com.example.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Класс для выполнения SQL-скриптов миграций и откатов.
 */
public class MigrationExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MigrationExecutor.class);
    private final MigrationFileReader fileReader;

    /**
     * Конструктор.
     */
    public MigrationExecutor() {
        this.fileReader = new MigrationFileReader();
    }

    /**
     * Выполняет миграции.
     *
     * @param connection подключение к базе данных.
     * @param migrationFiles список файлов миграций.
     */
    public void executeMigrations(Connection connection, List<String> migrationFiles) {
        logger.info("Начало выполнения миграций...");
        for (String migrationFile : migrationFiles) {
            try (Statement statement = connection.createStatement()) {
                String sql = fileReader.readFileContent(migrationFile);
                statement.executeUpdate(sql);
                logger.info("Миграция выполнена: {}", migrationFile);
            } catch (SQLException e) {
                logger.error("Ошибка выполнения миграции: {}", migrationFile, e);
            }
        }
        logger.info("Все миграции выполнены.");
    }

    /**
     * Выполняет откаты.
     *
     * @param connection подключение к базе данных.
     * @param rollbackFiles список файлов откатов.
     */
    public void executeRollbacks(Connection connection, List<String> rollbackFiles) {
        logger.info("Начало выполнения откатов...");
        for (String rollbackFile : rollbackFiles) {
            try (Statement statement = connection.createStatement()) {
                String sql = fileReader.readFileContent(rollbackFile);
                statement.executeUpdate(sql);
                logger.info("Откат выполнен: {}", rollbackFile);
            } catch (SQLException e) {
                logger.error("Ошибка выполнения отката: {}", rollbackFile, e);
            }
        }
        logger.info("Все откаты выполнены.");
    }
}
