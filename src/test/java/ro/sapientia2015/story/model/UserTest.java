package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import ro.sapientia2015.story.CommonTestUtil;

public class UserTest {

	private String USERNAME = "username";
    private String PASSWORD = "password";

    @Test
    public void buildWithMandatoryInformation() {
        User built = User.getBuilder(USERNAME)
        		.password(PASSWORD)
        		.build();

        assertNull(built.getId());
        assertEquals(USERNAME, built.getUsername());
        assertEquals(PASSWORD, built.getPassword());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        User user = new User();
        user.prePersist();

        assertNull(user.getId());
        assertNotNull(user.getCreationTime());
        assertNull(user.getPassword());
        assertNotNull(user.getModificationTime());
        assertNull(user.getUsername());
        assertEquals(0L, user.getVersion());
        assertEquals(user.getCreationTime(), user.getModificationTime());
    }

    @Test
    public void preUpdate() {
        User user = new User();
        user.prePersist();

        CommonTestUtil.pause(1000);

        user.preUpdate();

        assertNull(user.getId());
        assertNotNull(user.getCreationTime());
        assertNull(user.getPassword());
        assertNotNull(user.getModificationTime());
        assertNull(user.getUsername());
        assertEquals(0L, user.getVersion());
        assertTrue(user.getModificationTime().isAfter(user.getCreationTime()));
    }

}
