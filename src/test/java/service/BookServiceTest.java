package service;

import model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService service;

    @BeforeEach
    void setup() throws Exception {
        service = new BookService();

        var conn = DatabaseManager.getInstance().getConnection();
        conn.createStatement().execute("DELETE FROM books");
    }

    // ✅ Add book
    @Test
    void testAddBook() throws Exception {
        Book b = new Book();
        b.setCode("B100");
        b.setTitle("Test Book");
        b.setAuthor("Author");

        Book saved = service.addBook(b);

        assertEquals("B100", saved.getCode());
        assertEquals("Test Book", saved.getTitle());
    }

    @Test
    void testDuplicateBookCode() throws Exception {
        Book b1 = new Book();
        b1.setCode("DUP1");
        b1.setTitle("Book1");
        b1.setAuthor("A");

        service.addBook(b1);

        Book b2 = new Book();
        b2.setCode("DUP1");
        b2.setTitle("Book2");
        b2.setAuthor("B");

        Exception ex = assertThrows(Exception.class, () -> {
            service.addBook(b2);
        });

        assertEquals("Book with this code already exists", ex.getMessage());
    }

    // ✅ Get all books
    @Test
    void testGetAllBooks() throws Exception {
        List<Book> list = service.getAllBooks();

        assertNotNull(list);
        assertTrue(list.size() >= 0);
    }

    // ✅ Update book
    @Test
    void testUpdateBook() throws Exception {
        Book b = new Book();
        b.setCode("UPD1");
        b.setTitle("Before");
        b.setAuthor("Author");

        service.addBook(b);

        Book updated = new Book();
        updated.setCode("UPD1");
        updated.setTitle("After");
        updated.setAuthor("New Author");

        service.updateBook("UPD1", updated);

        List<Book> list = service.getAllBooks();

        boolean found = list.stream()
                .anyMatch(x -> x.getTitle().equals("After"));

        assertTrue(found);
    }



    // ✅ Delete book
    @Test
    void testDeleteBook() throws Exception {
        Book b = new Book();
        b.setCode("DEL1");
        b.setTitle("ToDelete");
        b.setAuthor("Author");

        service.addBook(b);

        service.deleteBook("DEL1");

        List<Book> list = service.getAllBooks();

        boolean exists = list.stream()
                .anyMatch(x -> x.getCode().equals("DEL1"));

        assertFalse(exists);
    }
}
