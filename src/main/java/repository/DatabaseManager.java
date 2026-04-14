package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "12345";

    private DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            createDatabaseIfNotExists();
            connect();
            initTables();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }


    private void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE library");

        } catch (SQLException e) {
//            if ("42P04".equals(e.getSQLState())) {
//                System.out.println("Database already exists");
//            } else {
//                e.printStackTrace();
//            }
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/library", USER, PASSWORD);
    }

    private void initTables() {
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS books (
                code VARCHAR PRIMARY KEY,
                title VARCHAR NOT NULL,
                author VARCHAR NOT NULL
            );
        """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS members (
                id SERIAL PRIMARY KEY,
                name VARCHAR NOT NULL,
                email VARCHAR UNIQUE NOT NULL,
                join_date DATE NOT NULL
            );
        """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS borrowings (
                book_code VARCHAR,
                member_id INT,
                borrow_date TIMESTAMP,
                return_date TIMESTAMP,
                PRIMARY KEY (book_code, member_id, borrow_date),
                FOREIGN KEY (book_code) REFERENCES books(code) ON DELETE CASCADE,
                FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
            );
        """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
