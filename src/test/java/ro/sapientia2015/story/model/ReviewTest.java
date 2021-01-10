package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.Review;
import static junit.framework.Assert.*;

public class ReviewTest {

	private String REVIEW = "Approved";
	private Long STORYID = (long) 1;

	@Test
	public void buildBasicReview() {
		Review review = Review.getBuilder(STORYID).build();

		assertNull(review.getId());
		assertNull(review.getCreationTime());
		assertNull(review.getModificationTime());
		assertNull(review.getReview());
		assertEquals(STORYID, review.getStoryId());
		assertEquals(0L, review.getVersion());
	}

	@Test
	public void builGoodReview() {
		Review review = Review.getBuilder(STORYID, REVIEW).build();

		assertNull(review.getId());
		assertNull(review.getCreationTime());
		assertNull(review.getModificationTime());
		assertEquals(STORYID, review.getStoryId());
		assertEquals(REVIEW, review.getReview());
		assertEquals(0L, review.getVersion());
	}

	@Test
	public void prePersist() {
		Review review = new Review();
		review.prePersist();

		assertNull(review.getId());
		assertNotNull(review.getCreationTime());
		assertNull(review.getReview());
		assertNotNull(review.getModificationTime());
		assertNull(review.getStoryId());
		assertEquals(0L, review.getVersion());
		assertEquals(review.getCreationTime(), review.getModificationTime());
	}

	@Test
	public void preUpdate() {
		Review review = new Review();
		review.prePersist();
		
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// Do Nothing
		}
		
		review.preUpdate();
		
		assertNull(review.getId());
		assertNotNull(review.getCreationTime());
		assertNull(review.getReview());
		assertNotNull(review.getModificationTime());
		assertNull(review.getStoryId());
		assertEquals(0L, review.getVersion());
		assertTrue(review.getModificationTime().isAfter(review.getCreationTime()));
	}

}
