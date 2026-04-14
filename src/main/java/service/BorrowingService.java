package service;

import model.Borrowing;
import repository.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowingService {
    private Connection conn = DatabaseManager.getInstance().getConnection();

    public List<Borrowing> getAll() throws SQLException {
        List<Borrowing> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM borrowings");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Borrowing b = new Borrowing();
            b.setBookCode(rs.getString("book_code"));
            b.setMemberId(rs.getInt("member_id"));
            b.setBorrowDate(rs.getTimestamp("borrow_date"));
            b.setReturnDate(rs.getTimestamp("return_date"));
            list.add(b);
        }

        return list;
    }

    public void borrowBook(String bookCode, int memberId) throws Exception {

        PreparedStatement bookCheck = conn.prepareStatement(
                "SELECT 1 FROM books WHERE code = ?");
        bookCheck.setString(1, bookCode);

        if (!bookCheck.executeQuery().next()) {
            throw new Exception("Book does not exist");
        }

        PreparedStatement memberCheck = conn.prepareStatement(
                "SELECT 1 FROM members WHERE id = ?");
        memberCheck.setInt(1, memberId);

        if (!memberCheck.executeQuery().next()) {
            throw new Exception("Member does not exist");
        }

        PreparedStatement check = conn.prepareStatement(
                "SELECT 1 FROM borrowings WHERE book_code = ? AND return_date IS NULL"
        );
        check.setString(1, bookCode);

        if (check.executeQuery().next()) {
            throw new Exception("Book is already borrowed");
        }

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO borrowings (book_code, member_id, borrow_date) VALUES (?, ?, NOW())"
        );

        stmt.setString(1, bookCode);
        stmt.setInt(2, memberId);

        stmt.executeUpdate();
    }

    public void returnBook(String bookCode) throws Exception {

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE borrowings " +
                        "SET return_date = NOW() " +
                        "WHERE book_code = ? AND return_date IS NULL"
        );

        stmt.setString(1, bookCode);

        int updated = stmt.executeUpdate();

        if (updated == 0) {
            throw new Exception("No active borrowing found for this book");
        }
    }
}
