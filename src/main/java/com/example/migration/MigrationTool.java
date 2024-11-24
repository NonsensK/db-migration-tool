package com.example.migration;

import java.sql.Connection;
import java.util.List; // Добавьте этот импорт

/**
 * Класс для запуска миграций и откатов через менеджер миграций.
 */
public class MigrationTool {

    private final MigrationManager migrationManager;

    public MigrationTool() {
        this.migrationManager = new MigrationManager();
    }

    public void migrate(Connection connection) {
        migrationManager.applyMigrations(connection, List.of(
                "migrations/V1_create_migration_history.sql",
                "migrations/V2__create_users_table.sql",
                "migrations/V3__add_index_to_users.sql"
        ));
    }

    public void rollback(Connection connection) {
        migrationManager.applyRollbacks(connection, List.of(
                "migrations/rollback/v1-rollback.sql",
                "migrations/rollback/v2-rollback.sql",
                "migrations/rollback/v3-rollback.sql"
        ));
    }
}
