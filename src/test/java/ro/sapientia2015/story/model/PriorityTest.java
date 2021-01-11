package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

/**
 * @author Kapas Krisztina
 */
public class PriorityTest {

    private String NAME = "name";

    @Test
    public void buildWithMandatoryInformation() {
    	Priority built = Priority.getBuilder(NAME).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
    	Priority built = Priority.getBuilder(NAME)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertEquals(NAME, built.getName());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
    	Priority priority = new Priority();
    	priority.prePersist();

        assertNull(priority.getId());
        assertNotNull(priority.getCreationTime());
        assertNotNull(priority.getModificationTime());
        assertNull(priority.getName());
        assertEquals(0L, priority.getVersion());
        assertEquals(priority.getCreationTime(), priority.getModificationTime());
    }

    @Test
    public void preUpdate() {
    	Priority priority = new Priority();
    	priority.prePersist();

        pause(1000);

        priority.preUpdate();

        assertNull(priority.getId());
        assertNotNull(priority.getCreationTime());
        assertNotNull(priority.getModificationTime());
        assertNull(priority.getName());
        assertEquals(0L, priority.getVersion());
        assertTrue(priority.getModificationTime().isAfter(priority.getCreationTime()));
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
