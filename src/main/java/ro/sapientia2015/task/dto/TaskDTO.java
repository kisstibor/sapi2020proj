package ro.sapientia2015.task.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.task.model.Task;

public class TaskDTO {

    private Long id;
    
    private Story story;

    @Length(max = Task.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Task.MAX_LENGTH_TITLE)
    private String title;

    public TaskDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Story getStory() {
    	return story;
    }
    
    public void setStory(Story story) {
    	this.story = story;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
