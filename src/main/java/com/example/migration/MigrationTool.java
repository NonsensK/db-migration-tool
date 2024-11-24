package com.example.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Главный класс для управления миграциями и откатами.
 */
public class MigrationTool {

    private final ConnectionManager connectionManager; // Управление подключениями.
    private final MigrationExecutor migrationExecutor; // Выполнение SQL-скриптов.
    private final MigrationFileReader migrationFileReader; // Чтение файлов миграций.

    /**
     * Конструктор для инициализации всех компонентов.
     */
    public MigrationTool() {
        this.connectionManager = new ConnectionManager(); // Читает параметры из application.properties.
        this.migrationExecutor = new MigrationExecutor();
        this.migrationFileReader = new MigrationFileReader("migrations"); // Указываем папку миграций.
    }

    /**
     * Метод для выполнения миграций.
     */
    public void migrate() {
        try (Connection connection = connectionManager.getConnection()) {
            // Получаем список файлов миграций.
            List<String> migrationFiles = migrationFileReader.readMigrationFiles();
            for (String migrationFile : migrationFiles) {
                // Для каждого файла вызываем метод executeMigration.
                migrationExecutor.executeMigration(connection, migrationFile);
            }
            System.out.println("Все миграции успешно применены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении миграций: " + e.getMessage());
        }
    }

    /**
     * Метод для выполнения откатов.
     */
    public void rollback() {
        try (Connection connection = connectionManager.getConnection()) {
            // Получаем список файлов откатов.
            List<String> rollbackFiles = migrationFileReader.readRollbackFiles();
            // Проходим по списку откатов в обратном порядке.
            for (int i = rollbackFiles.size() - 1; i >= 0; i--) {
                String rollbackFile = rollbackFiles.get(i);
                // Выполняем откат для каждого файла.
                migrationExecutor.executeRollback(connection, rollbackFile);
            }
            System.out.println("Откат миграций успешно завершён.");
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении отката: " + e.getMessage());
        }
    }

    /**
     * Метод для проверки статуса базы данных (заготовка, можно доработать).
     */
    public void checkStatus() {
        System.out.println("Функция проверки статуса еще не реализована.");
    }

    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        MigrationTool migrationTool = new MigrationTool();

        // Выполняем миграции.
        migrationTool.migrate();

        // Проверяем статус базы данных.
        migrationTool.checkStatus();

        // Откатываем миграцию (если нужно).
        migrationTool.rollback();
    }
}
