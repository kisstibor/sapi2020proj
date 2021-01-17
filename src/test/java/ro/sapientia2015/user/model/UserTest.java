package ro.sapientia2015.user.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;

import static junit.framework.Assert.*;

/**
 * @author Kiss Tibor
 */
public class UserTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";
    private Long EXPERIENCE = 0L;

    @Test
    public void buildWithMandatoryInformation() {
        User built = User.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
    	User built = User.getBuilder(TITLE)
                .description(DESCRIPTION)
                .experience(EXPERIENCE)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(EXPERIENCE, built.getExperience());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
    	User user = new User();
        user.prePersist();

        assertNull(user.getId());
        assertNotNull(user.getCreationTime());
        assertNull(user.getDescription());
        assertNotNull(user.getModificationTime());
        assertNull(user.getTitle());
        assertNull(user.getExperience());
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
        assertNull(user.getDescription());
        assertNotNull(user.getModificationTime());
        assertNull(user.getTitle());
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
