package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Member;
import service.MemberService;

import java.io.IOException;

public class MemberServlet extends HttpServlet {

    private MemberService service = new MemberService();
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            var list = service.getAll();

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), list);

        } catch (Exception e) {
            e.printStackTrace(); // 👈 ADD THIS (VERY IMPORTANT)

            resp.setStatus(500);
            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("error", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Member m = mapper.readValue(req.getInputStream(), Member.class);

            Member saved = service.add(m);

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), saved);

        } catch (Exception e) {
            resp.setStatus(422);
            resp.setContentType("application/json");

            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("error", e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo(); // /1
            int id = Integer.parseInt(path.substring(1));

            Member m = mapper.readValue(req.getInputStream(), Member.class);

            service.update(id, m);

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("message", "Updated"));
        } catch (Exception e) {
            resp.setStatus(422);
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo();
            int id = Integer.parseInt(path.substring(1));

            service.delete(id);

            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("message", "Updated"));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }
}