package com.example.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс для логирования миграций.
 */
public class MigrationLogger {
    // Создаем логгер, который будет записывать сообщения в журнал
    private static final Logger logger = LoggerFactory.getLogger(MigrationLogger.class);

    // Метод для логирования начала миграции
    public static void logMigrationStart(String migrationName) {
        logger.info("Начало миграции: " + migrationName);
    }

    // Метод для логирования успешного выполнения миграции
    public static void logMigrationSuccess(String migrationName) {
        logger.info("Миграция успешно выполнена: " + migrationName);
    }

    // Метод для логирования ошибки в миграции
    public static void logMigrationError(String migrationName, Exception e) {
        logger.error("Ошибка при выполнении миграции: " + migrationName, e);
    }
}
