package ro.sapientia2015.story.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;
import org.springframework.test.AssertThrows;

import ro.sapientia2015.story.CommentTestUtil;
import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

import java.util.ArrayList;

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
    
    @Test
    public void statusUpdates() {
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
    	
        Story built = Story.getBuilder(TITLE).description(DESCRIPTION).dueDate(now).build();
        assertEquals(StoryStatus.TODO, built.getStatus());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTodoStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTestingStatusTime());
        
        pause(1000);
        
        DateTime inProgress = DateTime.now();
        built.setStatus(StoryStatus.INPROGRESS);
        assertEquals(StoryStatus.INPROGRESS, built.getStatus());
        assertEquals(formatter.print(new Period(now,inProgress)), built.getTodoStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTestingStatusTime());
        
        
        pause(2000);
        
        DateTime inTesting = DateTime.now();
        built.setStatus(StoryStatus.UNDERTESTING);
        assertEquals(StoryStatus.UNDERTESTING, built.getStatus());
        assertEquals(formatter.print(new Period(now,inProgress)), built.getTodoStatusTime());
        assertEquals(formatter.print(new Period(inProgress,inTesting)), built.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), built.getTestingStatusTime());
        
        
        pause(3000);
        
        DateTime inDone = DateTime.now();
        built.setStatus(StoryStatus.DONE);
        assertEquals(StoryStatus.DONE, built.getStatus());
        assertEquals(formatter.print(new Period(now,inProgress)), built.getTodoStatusTime());
        assertEquals(formatter.print(new Period(inProgress,inTesting)), built.getProgressStatusTime());
        assertEquals(formatter.print(new Period(inTesting,inDone)), built.getTestingStatusTime());
    }

    @Test
    public void addComments() {
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();
        assertNotNull(built.getComments());
        assertEquals(0, built.getComments().size());

        Comment newComment = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
        built.addComment(newComment);
        
        assertEquals(1, built.getComments().size());
        assertEquals(CommentTestUtil.MESSAGE, built.getComments().get(0).getMessage());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeComments() {
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();
        Comment newComment = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
        built.addComment(newComment);
        assertEquals(1, built.getComments().size());
        assertEquals(CommentTestUtil.MESSAGE, built.getComments().get(0).getMessage());
        
        built.removeComment(newComment);
        
        assertEquals(0, built.getComments().size());
        built.getComments().get(0);
        
    }
    
    @Test
    public void setComments() {
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for(int i=0;i<3;++i) {
        	Comment newComment = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
        	comments.add(newComment);
        }
        assertEquals(0, built.getComments().size());
        
        
        built.setComments(comments);
        assertEquals(3, built.getComments().size());
        assertEquals(CommentTestUtil.MESSAGE, built.getComments().get(0).getMessage());
        assertEquals(CommentTestUtil.MESSAGE, built.getComments().get(2).getMessage());
        
    }
    
    @Test
    public void dueDateCheck() {
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();
        
        assertEquals(now.toString("dd MMMM yyyy, HH:mm"), built.getDueDate());
    }
    
    @Test
    public void statusChangeDateCheck() {
    	DateTime now = DateTime.now();
        Story built = Story.getBuilder(TITLE)
                .description(DESCRIPTION)
                .dueDate(now)
                .build();
        
        pause(100000);
        
        now = DateTime.now();
        built.setStatus(StoryStatus.INPROGRESS);
        assertEquals(now.toString("dd MMMM yyyy, HH:mm"), built.getStatusModificationTime());
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
