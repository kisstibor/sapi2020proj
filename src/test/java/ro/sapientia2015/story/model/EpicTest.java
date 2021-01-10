package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Hunor Szatmari
 */
public class EpicTest {
	
    private String TITLE = "title";
    private String DESCRIPTION = "description";

    @Test
    public void buildWithMandatoryInformation() {
        Epic built = Epic.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Epic built = Epic.getBuilder(TITLE)
                .description(DESCRIPTION)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        Epic epic = new Epic();
        epic.prePersist();

        assertNull(epic.getId());
        assertNotNull(epic.getCreationTime());
        assertNull(epic.getDescription());
        assertNotNull(epic.getModificationTime());
        assertNull(epic.getTitle());
        assertEquals(0L, epic.getVersion());
        assertEquals(epic.getCreationTime(), epic.getModificationTime());
    }

    @Test
    public void preUpdate() {
        Epic epic = new Epic();
        epic.prePersist();

        pause(1000);

        epic.preUpdate();

        assertNull(epic.getId());
        assertNotNull(epic.getCreationTime());
        assertNull(epic.getDescription());
        assertNotNull(epic.getModificationTime());
        assertNull(epic.getTitle());
        assertEquals(0L, epic.getVersion());
        assertTrue(epic.getModificationTime().isAfter(epic.getCreationTime()));
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
