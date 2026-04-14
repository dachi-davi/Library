package service;

import model.Member;
import repository.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private Connection conn = DatabaseManager.getInstance().getConnection();

    // 🔷 GET ALL
    public List<Member> getAll() throws SQLException {
        List<Member> list = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM members");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Member m = new Member();
            m.setId(rs.getInt("id"));
            m.setName(rs.getString("name"));
            m.setEmail(rs.getString("email"));
            m.setJoinDate(rs.getDate("join_date").toLocalDate());
            list.add(m);
        }

        return list;
    }

    // 🔷 ADD MEMBER
    public Member add(Member m) throws Exception {

        // ❗ email must be unique
        PreparedStatement check = conn.prepareStatement(
                "SELECT 1 FROM members WHERE email = ?");
        check.setString(1, m.getEmail());

        if (check.executeQuery().next()) {
            throw new Exception("Email already exists");
        }

        // ✅ insert (ID auto-generated)
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO members (name, email, join_date) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        stmt.setString(1, m.getName());
        stmt.setString(2, m.getEmail());
        stmt.setDate(3, Date.valueOf(LocalDate.now())); // auto date

        stmt.executeUpdate();

        // 🔥 get generated ID
        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) {
            m.setId(keys.getInt(1));
        }

        m.setJoinDate(LocalDate.now());

        return m;
    }

    // 🔷 UPDATE
    public void update(int id, Member m) throws Exception {

        // ❗ prevent duplicate email (excluding current user)
        PreparedStatement check = conn.prepareStatement(
                "SELECT 1 FROM members WHERE email = ? AND id != ?");
        check.setString(1, m.getEmail());
        check.setInt(2, id);

        if (check.executeQuery().next()) {
            throw new Exception("Email already exists");
        }

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE members SET name = ?, email = ? WHERE id = ?"
        );

        stmt.setString(1, m.getName());
        stmt.setString(2, m.getEmail());
        stmt.setInt(3, id);

        stmt.executeUpdate();
    }

    // 🔷 DELETE
    public void delete(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM members WHERE id = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
