package com.example.migration;

import java.io.BufferedReader; //Для чтения SQL-файлов строка за строкой
import java.io.IOException; //Исключение
import java.io.InputStream; //это базовый поток для чтения байтов. Используется для работы с файлами или ресурсами в памяти.
import java.io.InputStreamReader; //Мы работаем с текстовыми SQL-файлами, поэтому нам нужно преобразовать поток байтов в текст для обработки.
import java.util.ArrayList;// массивы, для того чтобы хранить содержимое SQL скриптов
import java.util.List; //список объектов, который можно использовать для работы с коллекциями.

public class MigrationFileReader {

    private final String migrationsPath; // Сохраняет путь к папке миграций, который передается в конструктор.

    public MigrationFileReader(String migrationsPath) {
        this.migrationsPath = migrationsPath;
        //При создании объекта MigrationFileReader мы передаем путь к папке с миграциями.
        // Например: resources/migrations.
    }


//Создает список sqlScripts, куда мы будем добавлять содержимое всех файлов миграций.
//Открывает папку с миграциями (resources/migrations).
//Читает каждый файл в папке.
//Добавляет содержимое каждого SQL-файла в список.
    public List<String> readMigrationFiles() {
        List<String> sqlScripts = new ArrayList<>();
        try {
            // Получаем список файлов в папке миграций
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(migrationsPath);
            if (resourceStream == null) {
                throw new IOException("Migration path not found: " + migrationsPath);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));
            String fileName;
            while ((fileName = reader.readLine()) != null) {
                InputStream fileStream = getClass().getClassLoader().getResourceAsStream(migrationsPath + "/" + fileName);
                if (fileStream != null) {
                    sqlScripts.add(readFileContent(fileStream));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading migration files: " + e.getMessage());
        }
        return sqlScripts;
    }

    private String readFileContent(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
        //Метод принимает поток (InputStream) файла.
        //Построчно читает файл и сохраняет его содержимое в строку.
        //Возвращает весь SQL-код как одну строку.
    }

    
}
