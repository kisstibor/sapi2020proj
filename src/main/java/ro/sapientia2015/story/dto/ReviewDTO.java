package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Review;

public class ReviewDTO {

	private Long id;

    @Length(max = Review.MAX_LENGTH_REVIEW)
    private String review;
    
    
    private Long storyId;

	public ReviewDTO() {

    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Long getStoryId() {
		return storyId;
	}

	public void setStoryId(Long storyId) {
		this.storyId = storyId;
	}


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
	
}
