package ro.sapientia2015.story.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.ReviewDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Review;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.ReviewRepository;

import javax.annotation.Resource;

import java.util.List;

@Service
public class RepositoryReviewService implements ReviewService{

    @Resource
    private ReviewRepository repository;
	
    @Transactional
    @Override
    public Review add(ReviewDTO added) {

        Review model = Review.getBuilder(added.getId())
                .review(added.getReview())
                .build();

        return repository.save(model);
    }
    
    @Transactional(rollbackFor = {NotFoundException.class})
   	@Override
	public Review deleteReviewById(Long id) throws NotFoundException {
    	Review deleted = findReviewByStoryId(id);
    	repository.delete(deleted);
		return null;
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public Review findReviewById(Long id) throws NotFoundException {
		Review found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

		return found;
	}

	@Override
	public Review findReviewByStoryId(Long id) throws NotFoundException {
		
		List<Review> reviews = repository.findAll();
		for (Review review : reviews) {
			if(review.getStoryId() == id) {
				return review;
			}
		}

		return null;
	}

    @Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Review update(ReviewDTO updated) throws NotFoundException {
    	Review review = findReviewById(updated.getId());
    	review.update(updated.getReview());
		return review;
	}

}
