package model;

import java.sql.Timestamp;

public class Borrowing {
    private String bookCode;
    private int memberId;
    private Timestamp borrowDate;
    private Timestamp returnDate;

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Timestamp getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Timestamp borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }
}
