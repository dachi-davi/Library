package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;
import service.BookService;
import java.io.IOException;
import java.util.List;

//@WebServlet("/books/*")
public class BookServlet extends HttpServlet {

    private BookService service = new BookService();
    private ObjectMapper mapper = new ObjectMapper();

    // 🔷 GET /books
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            List<Book> books = service.getAllBooks();

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), books);

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }

    // 🔷 POST /books
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            // JSON → Java
            Book book = mapper.readValue(req.getInputStream(), Book.class);

            Book saved = service.addBook(book);

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), saved);

        } catch (Exception e) {
            resp.setStatus(422); // requirement
            resp.getWriter().write(e.getMessage());
        }
    }

    // 🔷 PUT /books/{code}
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo(); // null now → we use request param instead

            String uri = req.getRequestURI();
            String code = uri.substring(uri.lastIndexOf("/") + 1);

//            String path = req.getPathInfo(); // /123
//            String code = path.substring(1);

            Book book = mapper.readValue(req.getInputStream(), Book.class);

            service.updateBook(code, book);

            resp.getWriter().write("Updated");

        } catch (Exception e) {
            resp.setStatus(422);
            resp.getWriter().write(e.getMessage());
        }
    }

    // 🔷 DELETE /books/{code}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo();
//            String code = path.substring(1);
            String uri = req.getRequestURI();
            String code = uri.substring(uri.lastIndexOf("/") + 1);

            service.deleteBook(code);

            resp.getWriter().write("Deleted");

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }
}