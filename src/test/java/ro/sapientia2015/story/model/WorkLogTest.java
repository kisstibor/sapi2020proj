package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.time.LocalDate;
import java.time.LocalTime;

import org.joda.time.DateTime;
import org.junit.Test;

public class WorkLogTest {
	private Long STORY_ID = Long.valueOf(1);
	private Long NEW_STORY_ID = Long.valueOf(2);
	private final String STORY_TITLE = "Test Story Title";
	private final String NEW_STORY_TITLE = "New Test Story Title";
	private final String DESCRIPTION = "Worklog Description";
	private final String NEW_DESCRIPTION = "New Worklog Description";
	private LocalDate LOGGED_AT = LocalDate.of(2021, 1, 12);
	private LocalDate NEW_LOGGED_AT = LocalDate.of(2022, 1, 12);
	private LocalTime STARTED_AT = LocalTime.of(10,15);
	private LocalTime NEW_STARTED_AT = LocalTime.of(11,15);
	private LocalTime ENDED_AT = LocalTime.of(11, 30);
	private LocalTime NEW_ENDED_AT = LocalTime.of(11, 45);
	
	@Test
    public void buildWithMandatoryInformation() {
        WorkLog built = WorkLog.getBuilder(STORY_ID, STORY_TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreatedAt());
        assertNull(built.getModifiedAt());
        assertNull(built.getLogged_at());
        assertNull(built.getStarted_at());
        assertNull(built.getEnded_at());
        assertNull(built.getDescription());
        assertEquals(STORY_ID, built.getStory_id());
        assertEquals(STORY_TITLE, built.getStory_title());
        assertEquals(0L, built.getVersion());
    }
	
	@Test
    public void buildWithAllInformation() {
		WorkLog built = WorkLog.getBuilder(STORY_ID, STORY_TITLE)
								.story_id(STORY_ID)
								.story_title(STORY_TITLE)
								.logged_at(LOGGED_AT)
								.started_at(STARTED_AT)
								.ended_at(ENDED_AT)
								.description(DESCRIPTION)
								.build();

		assertNull(built.getId());
        assertNull(built.getCreatedAt());
        assertNull(built.getModifiedAt());
        assertNotNull(built.getLogged_at());
        assertNotNull(built.getStarted_at());
        assertNotNull(built.getEnded_at());
        assertNotNull(built.getDescription());
        assertEquals(STORY_ID, built.getStory_id());
        assertEquals(STORY_TITLE, built.getStory_title());
        assertEquals(0L, built.getVersion());
    }
	
	@Test
    public void prePersist() {
		WorkLog worklog = new WorkLog();
		worklog.prePersist();

		assertNull(worklog.getId());
		assertNotNull(worklog.getCreatedAt());
		assertNotNull(worklog.getModifiedAt());
		assertNull(worklog.getLogged_at());
		assertNull(worklog.getStarted_at());
		assertNull(worklog.getEnded_at());
		assertNull(worklog.getDescription());
		assertNull(worklog.getStory_id());
		assertNull(worklog.getStory_title());
        assertEquals(0L, worklog.getVersion());
    }
	
	@Test
    public void preUpdate() {
		WorkLog worklog = new WorkLog();
		worklog.prePersist();

        pause(1000);

        worklog.preUpdate();

        assertNull(worklog.getId());
        assertNotNull(worklog.getCreatedAt());
		assertNotNull(worklog.getModifiedAt());
		assertNull(worklog.getLogged_at());
		assertNull(worklog.getStarted_at());
		assertNull(worklog.getEnded_at());
		assertNull(worklog.getDescription());
		assertNull(worklog.getStory_id());
		assertNull(worklog.getStory_title());
        assertEquals(0L, worklog.getVersion());
    }
	
	@Test
    public void Update() {
		WorkLog worklog = WorkLog.getBuilder(STORY_ID, STORY_TITLE)
								.story_id(STORY_ID)
								.story_title(STORY_TITLE)
								.logged_at(LOGGED_AT)
								.started_at(STARTED_AT)
								.ended_at(ENDED_AT)
								.description(DESCRIPTION)
								.build();

		Long oldId = worklog.getId();
		DateTime oldCreatedAt = worklog.getCreatedAt();
		DateTime oldModifiedAt = worklog.getModifiedAt();
		
		worklog.update(NEW_STORY_ID, NEW_STORY_TITLE, NEW_LOGGED_AT, NEW_STARTED_AT, NEW_ENDED_AT, NEW_DESCRIPTION);
		
		assertEquals(oldId, worklog.getId());
		assertEquals(oldCreatedAt, worklog.getCreatedAt());
		assertEquals(oldModifiedAt, worklog.getModifiedAt());
		assertEquals(worklog.getLogged_at(), NEW_LOGGED_AT);
		assertEquals(worklog.getStarted_at(), NEW_STARTED_AT);
		assertEquals(worklog.getEnded_at(), NEW_ENDED_AT);
		assertEquals(worklog.getDescription(), NEW_DESCRIPTION);
		assertEquals(worklog.getStory_id(), NEW_STORY_ID);
		assertEquals(worklog.getStory_title(), NEW_STORY_TITLE);
		assertEquals(0L, worklog.getVersion());
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
