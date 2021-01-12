package ro.sapientia2015.story.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;

public class CommentDTO {

    private Long id;

    @NotEmpty
    @Length(max = Comment.MAX_LENGTH_MESSAGE)
    private String message;

    @NotNull
    private Long storyId;

    public CommentDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long StoryId) {
        this.storyId = StoryId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
