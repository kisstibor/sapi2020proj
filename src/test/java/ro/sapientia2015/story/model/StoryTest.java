package ro.sapientia2015.story.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

/**
 * @author Kiss Tibor
 */
public class StoryTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";

    @Test
    public void buildWithMandatoryInformation() {
    	PeriodFormatter formatter = new PeriodFormatterBuilder()
      		     .appendDays()
      		     .appendSuffix("d")
      		     .appendHours()
      		     .appendSuffix("h")
      		     .appendMinutes()
      		     .appendSuffix("m")
      		     .appendSeconds()
      		     .appendSuffix("s")
      		     .toFormatter();
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE).dueDate(now).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(now, built.getDueDateAsDateTime());
        assertEquals(StoryStatus.TODO, built.getStatus());
        assertEquals(0L, built.getVersion());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTodoStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTestingStatusTime());
        assertNotNull(built.getComments());
    }

    @Test
    public void buildWithAllInformation() {
    	PeriodFormatter formatter = new PeriodFormatterBuilder()
   		     .appendDays()
   		     .appendSuffix("d")
   		     .appendHours()
   		     .appendSuffix("h")
   		     .appendMinutes()
   		     .appendSuffix("m")
   		     .appendSeconds()
   		     .appendSuffix("s")
   		     .toFormatter();
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(now, built.getDueDateAsDateTime());
        assertEquals(StoryStatus.TODO, built.getStatus());
        assertEquals(0L, built.getVersion());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTodoStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTestingStatusTime());
        assertNotNull(built.getComments());
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
        assertNull(story.getDueDateAsDateTime());
        assertNull(story.getStatus());
        assertEquals(0L, story.getVersion());
        assertEquals(story.getCreationTime(), story.getModificationTime());
        assertNull(story.getTodoStatusTime());
        assertNull(story.getProgressStatusTime());
        assertNull(story.getTestingStatusTime());
        assertNull(story.getComments());
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
        assertNull(story.getDueDateAsDateTime());
        assertNull(story.getStatus());
        assertNull(story.getTodoStatusTime());
        assertNull(story.getProgressStatusTime());
        assertNull(story.getTestingStatusTime());
        assertNull(story.getComments());
    }
    
    //NEED STATUS MODIFICATION TESTS

    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}
