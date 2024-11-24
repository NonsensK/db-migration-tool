package com.example.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection; // Для работы с подключением к базе данных.
import java.sql.Statement; // Для выполнения SQL-запросов.
import java.sql.SQLException; // Исключения при работе с базой данных.
import java.io.IOException; // Исключения при работе с файлами.
import java.nio.file.Files; // Для чтения содержимого файлов.
import java.nio.file.Paths; // Для работы с путями к файлам.

/**
 * Класс для выполнения SQL-скриптов миграций и откатов.
 */
public class MigrationExecutor {
    // Логгер для записи сообщений об ошибках и успешных действиях.
    private static final Logger logger = LoggerFactory.getLogger(MigrationExecutor.class);

    /**
     * Метод для выполнения миграции.
     * @param connection - подключение к базе данных.
     * @param migrationFilePath - путь к файлу миграции (SQL).
     */
    public void executeMigration(Connection connection, String migrationFilePath) {
        // Читаем содержимое SQL-скрипта из файла.
        String sql = readMigrationFile(migrationFilePath);

        // Проверяем, что SQL-скрипт не пустой и не содержит некорректных данных.
        if (sql == null || sql.trim().isEmpty()) {
            logger.error("Файл миграции пуст или некорректен: {}", migrationFilePath);
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Выполняем SQL-скрипт.
            statement.executeUpdate(sql);
            // Логируем успешное выполнение миграции.
            logger.info("Миграция выполнена успешно: {}", migrationFilePath);
        } catch (SQLException e) {
            // Логируем ошибку, если что-то пошло не так при выполнении SQL.
            logger.error("Ошибка при выполнении миграции (файл: {}): {}", migrationFilePath, e.getMessage(), e);
        }
    }

    /**
     * Метод для выполнения отката миграции.
     * @param connection - подключение к базе данных.
     * @param rollbackFilePath - путь к файлу отката (SQL).
     */
    public void executeRollback(Connection connection, String rollbackFilePath) {
        // Читаем содержимое SQL-скрипта отката из файла.
        String sql = readMigrationFile(rollbackFilePath);

        // Проверяем, что SQL-скрипт не пустой и корректный.
        if (sql == null || sql.trim().isEmpty()) {
            logger.error("Файл отката пуст или некорректен: {}", rollbackFilePath);
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // Выполняем SQL-скрипт отката.
            statement.executeUpdate(sql);
            // Логируем успешное выполнение отката.
            logger.info("Откат выполнен успешно: {}", rollbackFilePath);
        } catch (SQLException e) {
            // Логируем ошибку, если что-то пошло не так при выполнении SQL.
            logger.error("Ошибка при выполнении отката (файл: {}): {}", rollbackFilePath, e.getMessage(), e);
        }
    }

    /**
     * Читает SQL-скрипт из файла.
     * Этот метод используется для чтения содержимого SQL-файлов миграций и откатов.
     *
     * @param filePath - путь к файлу.
     * @return содержимое файла как строка.
     */
    private String readMigrationFile(String filePath) {
        try {
            // Чтение содержимого файла и преобразование его в строку.
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            // Логируем ошибку, если файл не найден или произошла другая ошибка ввода/вывода.
            logger.error("Ошибка при чтении файла: {} - {}", filePath, e.getMessage(), e);
            return null;
        }
    }
}
