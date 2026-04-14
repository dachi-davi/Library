package service;

import model.Book;
import repository.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private Connection conn = DatabaseManager.getInstance().getConnection();

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Book b = new Book();
            b.setCode(rs.getString("code"));
            b.setTitle(rs.getString("title"));
            b.setAuthor(rs.getString("author"));
            books.add(b);
        }

        return books;
    }


    public Book addBook(Book book) throws Exception {
        PreparedStatement check = conn.prepareStatement(
                "SELECT 1 FROM books WHERE code = ?");
        check.setString(1, book.getCode());

        if (check.executeQuery().next()) {
            throw new Exception("Book with this code already exists");
        }

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO books (code, title, author) VALUES (?, ?, ?)"
        );

        stmt.setString(1, book.getCode());
        stmt.setString(2, book.getTitle());
        stmt.setString(3, book.getAuthor());

        stmt.executeUpdate();

        return book;
    }

    public void deleteBook(String code) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM books WHERE code = ?");
        stmt.setString(1, code);
        stmt.executeUpdate();
    }

    public void updateBook(String code, Book book) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE books SET title = ?, author = ? WHERE code = ?"
        );

        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, code);

        stmt.executeUpdate();
    }
}
