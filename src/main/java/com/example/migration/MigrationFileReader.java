package com.example.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Класс для чтения SQL-файлов миграций и откатов.
 */
public class MigrationFileReader {
    private static final Logger logger = LoggerFactory.getLogger(MigrationFileReader.class);

    /**
     * Возвращает список всех файлов в указанной директории.
     *
     * @param directoryPath путь к директории.
     * @return список файлов.
     */
    public List<String> listFiles(String directoryPath) {
        logger.debug("Чтение файлов из директории: {}", directoryPath);
        List<String> fileList = new ArrayList<>();

        try {
            // Проверяем, запущено ли приложение из JAR-файла
            String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            if (jarPath.endsWith(".jar")) {
                // Чтение из JAR
                try (JarFile jarFile = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().startsWith(directoryPath) && !entry.isDirectory()) {
                            fileList.add(entry.getName());
                        }
                    }
                }
            } else {
                // Чтение из файловой системы
                InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(directoryPath);
                if (resourceStream == null) {
                    throw new IOException("Directory not found: " + directoryPath);
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
                    String fileName;
                    while ((fileName = reader.readLine()) != null) {
                        if (!fileName.endsWith("/")) { // Исключаем директории
                            fileList.add(directoryPath + "/" + fileName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка чтения файлов из директории: {}", directoryPath, e);
        }

        return fileList;
    }

    /**
     * Читает содержимое SQL-файла.
     *
     * @param filePath путь к файлу.
     * @return содержимое файла как строка.
     */
    public String readFileContent(String filePath) {
        logger.debug("Чтение содержимого файла: {}", filePath);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        } catch (IOException e) {
            logger.error("Ошибка чтения файла: {}", filePath, e);
            return null;
        }
    }
}
