package ro.sapientia2015.story.model;

import org.junit.Test;


import static junit.framework.Assert.*;

/**
 * @author Kiss Tibor
 */
public class UserTest {

    private String NAME = "title";
    private String TYPE = "type";

    @Test
    public void buildWithMandatoryInformation() {
        User built = User.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getType());
        assertNull(built.getModificationTime());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        User built = User.getBuilder(NAME)
                .type(TYPE)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(TYPE, built.getType());
        assertNull(built.getModificationTime());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        User user = new User();
        user.prePersist();

        assertNull(user.getId());
        assertNotNull(user.getCreationTime());
        assertNull(user.getType());
        assertNotNull(user.getModificationTime());
        assertNull(user.getName());
        assertEquals(0L, user.getVersion());
        assertEquals(user.getCreationTime(), user.getModificationTime());
    }

    @Test
    public void preUpdate() {
        User user = new User();
        user.prePersist();

        pause(1000);

        user.preUpdate();

        assertNull(user.getId());
        assertNotNull(user.getCreationTime());
        assertNull(user.getType());
        assertNotNull(user.getModificationTime());
        assertNull(user.getName());
        assertEquals(0L, user.getVersion());
        assertTrue(user.getModificationTime().isAfter(user.getCreationTime()));
    }

    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}
