package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BorrowingService;

import java.io.IOException;
import java.util.Map;

public class BorrowingServlet extends HttpServlet {

    private BorrowingService service = new BorrowingService();
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            var list = service.getAll();

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), list);

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String path = req.getPathInfo();

        try {
            Map<String, Object> body =
                    mapper.readValue(req.getInputStream(), Map.class);

            if (path != null && path.equals("/return")) {

                String bookCode = (String) body.get("bookCode");

                service.returnBook(bookCode);

                resp.getWriter().write("Book returned");
                return;
            }

            String bookCode = (String) body.get("bookCode");
            int memberId = (int) body.get("memberId");

            service.borrowBook(bookCode, memberId);

            resp.getWriter().write("Book borrowed");

        } catch (Exception e) {
            resp.setStatus(422);
            resp.getWriter().write(e.getMessage());
        }
    }
}
