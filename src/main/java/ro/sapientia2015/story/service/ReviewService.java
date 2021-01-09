package ro.sapientia2015.story.service;

import ro.sapientia2015.story.dto.ReviewDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Review;

public interface ReviewService {
	
    public Review add(ReviewDTO added);
	
	public Review deleteReviewById(Long id) throws NotFoundException;
	    
	public Review findReviewById(Long id) throws NotFoundException;
	
	public Review findReviewByStoryId(Long id) throws NotFoundException;
}
