package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class StoryTimeLimitTest {
	private static final Long STORY_ID = 1L;
    private static final String TIMELIMIT = "2020-02-01";

    @Test
    public void buildWithMandatoryInformation() {
        StoryTimeLimit built = StoryTimeLimit.getBuilder(STORY_ID).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertNull(built.getTimelimit());
        assertEquals(STORY_ID, built.getStoryId());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        StoryTimeLimit built = StoryTimeLimit.getBuilder(STORY_ID)
                .timelimit(TIMELIMIT)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertEquals(TIMELIMIT, built.getTimelimit());
        assertEquals(STORY_ID, built.getStoryId());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        StoryTimeLimit storytimelimit = new StoryTimeLimit();
        storytimelimit.prePersist();

        assertNull(storytimelimit.getId());
        assertNull(storytimelimit.getTimelimit());
        assertNull(storytimelimit.getStoryId());
        assertEquals(0L, storytimelimit.getVersion());
        
        assertNotNull(storytimelimit.getCreationTime());
        assertNotNull(storytimelimit.getModificationTime());
        assertEquals(storytimelimit.getCreationTime(), storytimelimit.getModificationTime());
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
        assertTrue(story.getModificationTime().isAfter(story.getCreationTime()));
        
        StoryTimeLimit storytimelimit = new StoryTimeLimit();
        storytimelimit.prePersist();
        
        pause(1000);
        storytimelimit.preUpdate();

        assertNull(storytimelimit.getId());
        assertNull(storytimelimit.getTimelimit());
        assertNull(storytimelimit.getStoryId());
        assertEquals(0L, storytimelimit.getVersion());
        
        assertNotNull(storytimelimit.getCreationTime());
        assertNotNull(storytimelimit.getModificationTime());
        assertTrue(storytimelimit.getCreationTime().isBefore(storytimelimit.getModificationTime()));
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
