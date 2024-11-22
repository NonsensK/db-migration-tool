package com.example.migration;

public class Main {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в инструмент миграций!");

        // Создаем объект MigrationTool
        MigrationTool migrationTool = new MigrationTool();

        // Выполняем миграции
        System.out.println("Запуск миграций...");
        migrationTool.migrate();

        // Проверяем статус базы данных
        System.out.println("Проверка статуса базы данных...");
        migrationTool.checkStatus();

        // Заглушка для отката миграции
        System.out.println("Откат миграции...");
        migrationTool.rollback();
    }
}
