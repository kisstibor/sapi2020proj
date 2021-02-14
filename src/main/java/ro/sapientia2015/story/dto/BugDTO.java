package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Bug;

public class BugDTO {

    private Long id;
    
    @NotEmpty
    @Length(max = Bug.MAX_LENGTH_TITLE)
    private String title;
    
    @Length(max = Bug.MAX_LENGTH_DESCRIPTION)
    private String description;
    
    @Length(max = Bug.MAX_LENGTH_STATUS)
    private String status;
    
    public BugDTO() {
    	
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status; 
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}