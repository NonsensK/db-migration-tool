package com.example.migration;

import java.io.BufferedReader; // Для чтения SQL-файлов строка за строкой.
import java.io.IOException; // Исключения, связанные с I/O операциями.
import java.io.InputStream; // Поток для чтения байтов из ресурсов.
import java.io.InputStreamReader; // Преобразует байтовый поток в символьный.
import java.util.ArrayList; // Используется для хранения списка SQL-скриптов.
import java.util.List; // Коллекция для работы с массивами и списками.

/**
 * Класс для чтения SQL-файлов миграций и откатов из указанных директорий.
 * Читает содержимое SQL-скриптов и возвращает их в виде строк.
 */
public class MigrationFileReader {

    private final String migrationsPath; // Путь к папке миграций.

    /**
     * Конструктор, принимает путь к папке миграций.
     * Например: resources/migrations.
     *
     * @param migrationsPath путь к папке миграций.
     */
    public MigrationFileReader(String migrationsPath) {
        this.migrationsPath = migrationsPath;
    }

    /**
     * Метод для чтения SQL-файлов миграций.
     * Открывает папку с миграциями (например, resources/migrations),
     * читает файлы и добавляет их содержимое в список.
     *
     * @return список SQL-скриптов миграций.
     */
    public List<String> readMigrationFiles() {
        List<String> sqlScripts = new ArrayList<>();
        try {
            // Получаем список файлов в папке миграций.
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(migrationsPath);
            if (resourceStream == null) {
                throw new IOException("Migration path not found: " + migrationsPath);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
            String fileName;
            while ((fileName = reader.readLine()) != null) {
                // Для каждого файла читаем его содержимое.
                InputStream fileStream = getClass().getClassLoader().getResourceAsStream(migrationsPath + "/" + fileName);
                if (fileStream != null) {
                    sqlScripts.add(readFileContent(fileStream));
                }
            }
        } catch (IOException e) {
            // Логируем ошибку, если файл не найден или произошла ошибка чтения.
            System.err.println("Error reading migration files: " + e.getMessage());
        }
        return sqlScripts; // Возвращаем список всех прочитанных скриптов миграций.
    }

    /**
     * Метод для чтения SQL-файлов откатов из папки rollback.
     * Папка rollback содержит файлы откатов, которые позволяют отменить изменения,
     * сделанные миграциями. Этот метод возвращает список SQL-скриптов откатов.
     *
     * @return список SQL-скриптов откатов.
     */
    public List<String> readRollbackFiles() {
        // Создаем список для хранения содержимого SQL-скриптов откатов
        List<String> rollbackScripts = new ArrayList<>();

        try {
            // Находим папку rollback внутри указанного пути миграций
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(migrationsPath + "/rollback");

            // Если папка rollback не найдена, выбрасываем исключение
            if (resourceStream == null) {
                throw new IOException("Rollback path not found: " + migrationsPath + "/rollback");
            }

            // Читаем список файлов из папки rollback
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
            String fileName;

            // Проходим по каждому файлу в папке rollback
            while ((fileName = reader.readLine()) != null) {
                // Находим полный путь к файлу отката
                InputStream fileStream = getClass().getClassLoader().getResourceAsStream(migrationsPath + "/rollback/" + fileName);

                // Если файл найден, читаем его содержимое
                if (fileStream != null) {
                    rollbackScripts.add(readFileContent(fileStream)); // Добавляем содержимое файла в список
                }
            }
        } catch (IOException e) {
            // Если произошла ошибка (например, файл не найден или проблемы с доступом),
            // выводим сообщение об ошибке в консоль
            System.err.println("Error reading rollback files: " + e.getMessage());
        }

        // Возвращаем список SQL-скриптов откатов
        return rollbackScripts;
    }

    /**
     * Метод для чтения содержимого SQL-файла.
     * Построчно читает файл и сохраняет его содержимое в строку.
     *
     * @param inputStream поток файла.
     * @return содержимое файла в виде строки.
     * @throws IOException если произошла ошибка при чтении файла.
     */
    private String readFileContent(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString(); // Возвращаем содержимое файла как одну строку.
    }
}
