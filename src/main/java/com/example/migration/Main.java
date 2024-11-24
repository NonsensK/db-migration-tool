package com.example.migration;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в инструмент миграций!");

        ConnectionManager connectionManager = new ConnectionManager();
        try (Connection connection = connectionManager.getConnection()) {
            MigrationTool tool = new MigrationTool();

            System.out.println("Запуск миграций...");
            tool.migrate(connection);

            System.out.println("Запуск откатов...");
            tool.rollback(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
