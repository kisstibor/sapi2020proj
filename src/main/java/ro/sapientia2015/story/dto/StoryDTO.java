package ro.sapientia2015.story.dto;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryStatus;

/**
 * @author Kiss Tibor
 */
public class StoryDTO {

    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Story.MAX_LENGTH_TITLE)
    private String title;
    
    private StoryStatus status;
    
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private DateTime dueDate;
    
    private List<Comment> comments;
    
    

    public StoryDTO() {

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
    
    public StoryStatus getStatus() {
        return status;
    }

    public void setStatus(StoryStatus newStatus) {
        this.status = newStatus;
    }
    
    public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

    public List<Comment> getComments() {
		return comments;
	}

    public String getDueDate2() {
        return dueDate.toString(DateTimeFormat.forPattern("dd MMMM yyyy, HH:mm"));
    }
    
    public DateTime getDueDateAsDateTime() {
        return dueDate;
    }
    
    /*public void setDueDate(String date) {							// 2020-12-31
    	this.dueDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(date);
    }*/
    
    public void setDueDate(Date date) {							// 2020-12-31
    	this.dueDate = new DateTime(date);
    }
    
    public DateTime getDueDate() {
        return dueDate;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
