package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Member;
import service.MemberService;

import java.io.IOException;

//@WebServlet("/members/*")
public class MemberServlet extends HttpServlet {

    private MemberService service = new MemberService();
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    // 🔷 GET /members
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

    // 🔷 POST /members
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Member m = mapper.readValue(req.getInputStream(), Member.class);

            Member saved = service.add(m);

            // ✅ ONLY write here (success)
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), saved);

        } catch (Exception e) {
            // ❗ DO NOT reset
            resp.setStatus(422);
            resp.setContentType("application/json");

            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("error", e.getMessage()));
        }
    }

    // 🔷 PUT /members/{id}
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo(); // /1
            int id = Integer.parseInt(path.substring(1));

            Member m = mapper.readValue(req.getInputStream(), Member.class);

            service.update(id, m);

//            resp.getWriter().write("Updated");
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("message", "Updated"));
        } catch (Exception e) {
            resp.setStatus(422);
            resp.getWriter().write(e.getMessage());
        }
    }

    // 🔷 DELETE /members/{id}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String path = req.getPathInfo();
            int id = Integer.parseInt(path.substring(1));

            service.delete(id);

//            resp.getWriter().write("Deleted");
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(),
                    java.util.Map.of("message", "Updated"));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(e.getMessage());
        }
    }
}