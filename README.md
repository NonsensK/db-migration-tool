# DB Migration Tool

This project is a library for managing database migrations using JDBC. It helps apply, track, and rollback database changes.

## Features
- **Apply migrations**: Apply SQL migrations to your database.
- **Version control**: Track which migrations have been applied.
- **Rollback**: Rollback migrations to a specific version.

## Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/db-migration-tool.git
    cd db-migration-tool
    ```

2. Configure the database connection in `src/main/resources/application.properties`:

    ```properties
    db.url=jdbc:postgresql://localhost:5432/migration_db
    db.username=postgres
    db.password=password
    ```

3. To apply migrations, run:

    ```bash
    java -jar target/db-migration-tool.jar
    ```

## License
MIT License

