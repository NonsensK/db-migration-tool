package com.example.migration;

import java.sql.Connection;
import java.util.List;

/**
 * Класс для управления миграциями.
 */
public class MigrationManager {

    private final MigrationExecutor migrationExecutor;

    public MigrationManager() {
        this.migrationExecutor = new MigrationExecutor();
    }

    public void applyMigrations(Connection connection, List<String> migrationFiles) {
        migrationExecutor.executeMigrations(connection, migrationFiles);
    }

    public void applyRollbacks(Connection connection, List<String> rollbackFiles) {
        migrationExecutor.executeRollbacks(connection, rollbackFiles);
    }
}
