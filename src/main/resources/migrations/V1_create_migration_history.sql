CREATE TABLE migration_history (
             id SERIAL PRIMARY KEY,                -- Уникальный идентификатор
             migration_name VARCHAR(255) NOT NULL UNIQUE, -- Имя файла миграции
             applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Время выполнения миграции
);
