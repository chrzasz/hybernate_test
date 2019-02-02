package util;

import org.hibernate.Session;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SessionUtilTest {

    @Test
    public void testGetSession() {
        try(Session session = SessionUtil.getSession()) {
            assertNotNull(session);
        }
    }
}