package com.example.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MigrationManager {
    private static final MigrationExecutor migrationExecutor = new MigrationExecutor();

    // Метод для получения списка примененных миграций
    private List<String> getAppliedMigrations(Connection connection) {
        // Получаем список миграций, которые были уже применены (например, из базы данных)
        List<String> appliedMigrations = new ArrayList<>();
        // Здесь нужно выполнить запрос к базе данных, чтобы узнать примененные миграции
        // Например, SELECT * FROM applied_migrations
        // Вернем список миграций, которые были применены
        return appliedMigrations;
    }

    // Метод для применения миграции
    public void applyMigrations(Connection connection, List<String> migrationFiles) {
        List<String> appliedMigrations = getAppliedMigrations(connection);

        for (String migrationFile : migrationFiles) {
            if (!appliedMigrations.contains(migrationFile)) {
                // Если миграция еще не была применена, выполняем ее
                migrationExecutor.executeMigration(connection, migrationFile);
                // После выполнения миграции, добавляем ее в список примененных
                markMigrationAsApplied(connection, migrationFile);
            }
        }
    }

    // Метод для пометки миграции как примененной в базе данных
    private void markMigrationAsApplied(Connection connection, String migrationFile) {
        // Добавляем запись о примененной миграции в базу данных
        String sql = "INSERT INTO applied_migrations (migration_name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, migrationFile);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении миграции в базу данных: " + e.getMessage());
        }
    }
}
//Подключение к базе данных: В этом классе мы предполагаем, что подключение к базе данных уже существует,
//и передаем его в методы для работы с данными.

//Использование PreparedStatement: Мы используем PreparedStatement для безопасного выполнения SQL-запросов,
//чтобы избежать SQL-инъекций.

//Логика миграций: Мы отслеживаем, какие миграции были применены,
//чтобы не применять одну и ту же миграцию несколько раз. Это важная часть работы с миграциями.
