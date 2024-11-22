package com.example.migration;

import org.slf4j.Logger;//это интерфейс, который предоставляет методы для записи сообщений в журнал, такие как:
//debug()
// info()
// warn()
//error()
import org.slf4j.LoggerFactory;//используется для создания экземпляра логгера в вашем проекте.
// Он предоставляет методы для создания и настройки логгера, который будет записывать сообщения в журнал (лог).

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
