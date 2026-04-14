package service;

import model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MemberServiceTest {

    private MemberService service;

    @BeforeEach
    void setup() {
        service = new MemberService();
    }

    @Test
    void testAddMember() throws Exception {
        Member m = new Member();
        m.setName("Test User");
        m.setEmail("test1@gmail.com");

        Member saved = service.add(m);

        assertNotNull(saved.getId());
        assertEquals("Test User", saved.getName());
    }

    @Test
    void testDuplicateEmail() throws Exception {
        Member m1 = new Member();
        m1.setName("User1");
        m1.setEmail("duplicate@gmail.com");

        service.add(m1);

        Member m2 = new Member();
        m2.setName("User2");
        m2.setEmail("duplicate@gmail.com");

        Exception ex = assertThrows(Exception.class, () -> {
            service.add(m2);
        });

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void testGetAll() throws Exception {
        List<Member> list = service.getAll();

        assertNotNull(list);
        assertTrue(list.size() >= 0);
    }

    @Test
    void testUpdateMember() throws Exception {
        Member m = new Member();
        m.setName("Before");
        m.setEmail("update@gmail.com");

        Member saved = service.add(m);

        Member updated = new Member();
        updated.setName("After");
        updated.setEmail("update_new@gmail.com");

        service.update(saved.getId(), updated);

        List<Member> list = service.getAll();

        boolean found = list.stream()
                .anyMatch(x -> x.getName().equals("After"));

        assertTrue(found);
    }

    @Test
    void testUpdateDuplicateEmail() throws Exception {
        Member m1 = new Member();
        m1.setName("A");
        m1.setEmail("a@gmail.com");
        Member saved1 = service.add(m1);

        Member m2 = new Member();
        m2.setName("B");
        m2.setEmail("b@gmail.com");
        Member saved2 = service.add(m2);

        Member update = new Member();
        update.setName("B updated");
        update.setEmail("a@gmail.com"); // duplicate

        Exception ex = assertThrows(Exception.class, () -> {
            service.update(saved2.getId(), update);
        });

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void testDeleteMember() throws Exception {
        Member m = new Member();
        m.setName("ToDelete");
        m.setEmail("delete@gmail.com");

        Member saved = service.add(m);

        service.delete(saved.getId());

        List<Member> list = service.getAll();

        boolean exists = list.stream()
                .anyMatch(x -> x.getId() == saved.getId());

        assertFalse(exists);
    }
}

