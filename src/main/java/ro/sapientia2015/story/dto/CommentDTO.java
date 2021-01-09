package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Comment;

public class CommentDTO {

    private Long id;

    @Length(max = Comment.MAX_LENGTH_DESCRIPTION)
    @NotEmpty
    private String message;

    private Long storyId;
    
    public CommentDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long id) {
        this.storyId = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
