package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.project.model.Epic;

import static junit.framework.Assert.*;


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
        Epic Epic = new Epic();
        Epic.prePersist();

        assertNull(Epic.getId());
        assertNotNull(Epic.getCreationTime());
        assertNull(Epic.getDescription());
        assertNotNull(Epic.getModificationTime());
        assertNull(Epic.getTitle());
        assertEquals(0L, Epic.getVersion());
        assertEquals(Epic.getCreationTime(), Epic.getModificationTime());
    }

    @Test
    public void preUpdate() {
        Epic Epic = new Epic();
        Epic.prePersist();

        pause(1000);

        Epic.preUpdate();

        assertNull(Epic.getId());
        assertNotNull(Epic.getCreationTime());
        assertNull(Epic.getDescription());
        assertNotNull(Epic.getModificationTime());
        assertNull(Epic.getTitle());
        assertEquals(0L, Epic.getVersion());
        assertTrue(Epic.getModificationTime().isAfter(Epic.getCreationTime()));
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
