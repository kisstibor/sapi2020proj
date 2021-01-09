package ro.sapientia2015.story.service;

import ro.sapientia2015.story.repository.ReviewRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.ReviewDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Review;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RepositoryReviewServiceTest {

	public static final Long ID = 1L;
	public static final Long STORYID = 5L;
	public static final String REVIEW = "Approved";

	private RepositoryReviewService service;

	private ReviewRepository repositoryMock;

	@Before
	public void setUp() {
		service = new RepositoryReviewService();

		repositoryMock = mock(ReviewRepository.class);
		ReflectionTestUtils.setField(service, "repository", repositoryMock);
	}

	@Test
	public void add() {
		ReviewDTO dto = new ReviewDTO();
		dto.setId(null);
		dto.setReview(REVIEW);
		dto.setStoryId(STORYID);

		service.add(dto);

		ArgumentCaptor<Review> reviewArgument = ArgumentCaptor.forClass(Review.class);
		verify(repositoryMock, times(1)).save(reviewArgument.capture());
		verifyNoMoreInteractions(repositoryMock);

		Review reviewModel = reviewArgument.getValue();

		assertEquals(dto.getStoryId(), reviewModel.getStoryId());
		assertEquals(dto.getReview(), reviewModel.getReview());
		assertNull(reviewModel.getId());
	}

	@Test
	public void findById() throws NotFoundException {

		Review review = new Review(ID, REVIEW, STORYID);

		when(repositoryMock.findOne(ID)).thenReturn(review);

		Review actualReview = service.findReviewById(ID);

		verify(repositoryMock, times(1)).findOne(ID);
		verifyNoMoreInteractions(repositoryMock);

		assertEquals(review, actualReview);
	}

	@Test
	public void findByStoryId() throws NotFoundException {

		List<Review> reviewList = new ArrayList<Review>();
		Review model = new Review(ID, REVIEW, STORYID);
		reviewList.add(model);

		when(repositoryMock.findAll()).thenReturn(reviewList);

		Review actualReview = service.findReviewByStoryId(STORYID);

		assertEquals(model, actualReview);
	}

	@Test
	public void deleteById() throws NotFoundException {

		List<Review> reviewList = new ArrayList<Review>();
		Review model = new Review(ID, REVIEW, STORYID);
		reviewList.add(model);

		when(repositoryMock.findAll()).thenReturn(reviewList);

		Review actualReview = service.deleteReviewById(STORYID);

		assertEquals(model, actualReview);

	}
}
