package com.example.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MigrationTool {

    private final ConnectionManager connectionManager;
    private final MigrationExecutor migrationExecutor;
    private final MigrationFileReader migrationFileReader;

    // Конструктор для инициализации всех компонентов
    public MigrationTool() {
        this.connectionManager = new ConnectionManager(
                "jdbc:postgresql://localhost:5432/mydatabase", // URL базы данных
                "your-username", // Логин
                "your-password"  // Пароль
        );
        this.migrationExecutor = new MigrationExecutor();
        this.migrationFileReader = new MigrationFileReader("migrations"); // Путь к папке миграций
    }

    // Метод для выполнения миграций
    public void migrate() {
        try (Connection connection = connectionManager.getConnection()) {
            // Получаем список файлов миграций
            List<String> migrationFiles = migrationFileReader.readMigrationFiles();
            for (String migrationFile : migrationFiles) {
                // Для каждого файла вызываем ваш метод executeMigration
                migrationExecutor.executeMigration(connection, migrationFile);
            }
            System.out.println("Все миграции успешно применены.");
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении миграций: " + e.getMessage());
        }
    }

    // Метод для проверки статуса базы данных (заготовка, можно доработать)
    public void checkStatus() {
        System.out.println("Функция проверки статуса еще не реализована.");
    }

    // Метод для отката миграции (заглушка, можно доработать)
    public void rollback() {
        System.out.println("Функция отката миграции еще не реализована.");
    }

    // Точка входа в приложение
    public static void main(String[] args) {
        MigrationTool migrationTool = new MigrationTool();

        // Выполняем миграции
        migrationTool.migrate();

        // Проверяем статус базы данных
        migrationTool.checkStatus();

        // Откатываем миграцию (если нужно)
        migrationTool.rollback();
    }
}
