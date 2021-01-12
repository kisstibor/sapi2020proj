package ro.sapientia2015.story.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class CommentTest {
	private String MESSAGE = "message";
	private Story STORY = Story.getBuilder("test").build();

	@Test
	public void buildWithMandatoryInformation() {
		Comment built = Comment.getBuilder("message").build();

		assertNull(built.getId());
		assertNull(built.getCreationTime());
		assertNull(built.getModificationTime());
		assertNull(built.getStory());
		assertEquals(MESSAGE, built.getMessage());
		assertEquals(0L, built.getVersion());
	}

	@Test
	public void buildWithAllInformation() {
		Comment built = Comment.getBuilder("message").story(STORY).build();

		assertNull(built.getId());
		assertNull(built.getCreationTime());
		assertEquals(MESSAGE, built.getMessage());
		assertNull(built.getModificationTime());
		assertEquals(STORY, built.getStory());
		assertEquals(0L, built.getVersion());
	}

	@Test
	public void prePersist() {
		Comment comment = new Comment();
		comment.prePersist();

		assertNull(comment.getId());
		assertNotNull(comment.getCreationTime());
		assertNull(comment.getMessage());
		assertNotNull(comment.getModificationTime());
		assertNull(comment.getStory());
		assertEquals(0L, comment.getVersion());
		assertEquals(comment.getCreationTime(), comment.getModificationTime());
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
		assertNull(comment.getStory());
		assertEquals(0L, comment.getVersion());
		assertTrue(comment.getModificationTime().isAfter(comment.getCreationTime()));
	}

	private void pause(long timeInMillis) {
		try {
			Thread.currentThread().sleep(timeInMillis);
		} catch (InterruptedException e) {
			// Do Nothing
		}
	}
}
