package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;

public class CommentTest {

	private String MESSAGE = "test message";
	
    @Test
    public void buildWithMandatoryInformation() {
        Comment built = Comment.getBuilder().build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getMessage());
        assertEquals(0L, built.getVersion());
        assertNull(built.getStory());
    }

    @Test
    public void buildWithAllInformation() {
        Comment built = Comment.getBuilder().message(MESSAGE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(MESSAGE, built.getMessage());
        assertEquals(0L, built.getVersion());
        assertNull(built.getStory());
    }

    @Test
    public void prePersist() {
    	Comment comment = new Comment();
        comment.prePersist();

        assertNull(comment.getId());
        assertNotNull(comment.getCreationTime());
        assertNull(comment.getMessage());
        assertNotNull(comment.getModificationTime());
        assertEquals(0L, comment.getVersion());
        assertEquals(comment.getCreationTime().toString(DateTimeFormat.forPattern("dd MMMM yyyy, HH:mm")), comment.getModificationTime());
        assertNull(comment.getStory());
    }
    

    @Test
    public void preUpdate() {
        Comment comment = new Comment();
        comment.prePersist();

        pause(1000);

        comment.preUpdate();

        assertNull(comment.getId());
        assertNotNull(comment.getCreationTime());
        assertNull(comment.getMessage());
        assertNotNull(comment.getModificationTime());
        assertEquals(0L, comment.getVersion());
        assertTrue(comment.getModificationTimeAsDateTime().isAfter(comment.getCreationTime()));
        assertNull(comment.getStory());
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
