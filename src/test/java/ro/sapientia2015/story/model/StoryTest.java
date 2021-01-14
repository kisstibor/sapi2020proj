package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

/**
 * @author Kiss Tibor
 */
public class StoryTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";
    private Integer PROGRESS = 50;

    @Test
    public void buildWithMandatoryInformation() {
        Story built = Story.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertNull(built.getProgress());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .progress(PROGRESS)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(PROGRESS, built.getProgress());
        assertEquals(0L, built.getVersion());
    }
    

    @Test
    public void prePersist() {
        Story story = new Story();
        story.prePersist();

        assertNull(story.getId());
        assertNotNull(story.getCreationTime());
        assertNull(story.getDescription());
        assertNotNull(story.getModificationTime());
        assertNull(story.getTitle());
        assertNull(story.getProgress());
        assertEquals(0L, story.getVersion());
        assertEquals(story.getCreationTime(), story.getModificationTime());
    }

    @Test
    public void preUpdate() {
        Story story = new Story();
        story.prePersist();

        pause(1000);

        story.preUpdate();

        assertNull(story.getId());
        assertNotNull(story.getCreationTime());
        assertNull(story.getDescription());
        assertNotNull(story.getModificationTime());
        assertNull(story.getTitle());
        assertEquals(0L, story.getVersion());
        assertNull(story.getProgress());
        assertTrue(story.getModificationTime().isAfter(story.getCreationTime()));
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
