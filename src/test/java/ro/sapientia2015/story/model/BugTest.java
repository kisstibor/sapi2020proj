package ro.sapientia2015.story.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BugTest {

	private String TITLE = "title";
	private String DESCRIPTION = "description";
	private String STATUS = "status";

	@Test
	public void buildWithMandatoryInformation() {
		Bug built = Bug.getBuilder(TITLE).build();

		assertNull(built.getId());
		assertNull(built.getCreationTime());
		assertNull(built.getDescription());
		assertNull(built.getModificationTime());
		assertEquals(TITLE, built.getTitle());
		assertEquals(0L, built.getVersion());
	}

	@Test
	public void buildWithAllInformation() {
		Bug built = Bug.getBuilder(TITLE).description(DESCRIPTION).status(STATUS).build();

		assertNull(built.getId());
		assertNull(built.getCreationTime());
		assertEquals(DESCRIPTION, built.getDescription());
		assertNull(built.getModificationTime());
		assertEquals(TITLE, built.getTitle());
		assertEquals(0L, built.getVersion());
		assertEquals(STATUS, built.getStatus());
	}

	@Test
	public void prePersist() {
		Bug bug = new Bug();
		bug.prePersist();

		assertNull(bug.getId());
		assertNotNull(bug.getCreationTime());
		assertNull(bug.getDescription());
		assertNotNull(bug.getModificationTime());
		assertNull(bug.getTitle());
		assertEquals(0L, bug.getVersion());
		assertNull(bug.getStatus());
		assertEquals(bug.getCreationTime(), bug.getModificationTime());
	}

	@Test
	public void preUpdate() {
		Bug bug = new Bug();
		bug.prePersist();

		pause(1000);

		bug.preUpdate();

		assertNull(bug.getId());
		assertNotNull(bug.getCreationTime());
		assertNull(bug.getDescription());
		assertNotNull(bug.getModificationTime());
		assertNull(bug.getTitle());
		assertEquals(0L, bug.getVersion());
		assertNull(bug.getStatus());
		assertTrue(bug.getModificationTime().isAfter(bug.getCreationTime()));
	}

	private void pause(long timeInMillis) {
		try {
			Thread.currentThread().sleep(timeInMillis);
		} catch (InterruptedException e) {
			// Do Nothing
		}
	}

}
