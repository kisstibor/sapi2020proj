package ro.sapientia2015.task.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.task.model.Task;

public class TaskTest {
	
	private String TITLE = "title";
	private String DESCRIPTION = "description";
	private  Story story = Story.getBuilder(TITLE).description(DESCRIPTION).build();

    @Test
    public void buildWithMandatoryInformation() {
        Task built = Task.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
        Task built = Task.getBuilder(TITLE)
                .story(story)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(story, built.getStory());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }
}
