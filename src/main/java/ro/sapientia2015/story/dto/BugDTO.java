package ro.sapientia2015.story.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Bug;

/**
 * @author Zsok Andrei
 */
public class BugDTO {
	
	private Long id;
	
	@NotEmpty
    @Length(max = Bug.MAX_LENGTH_TITLE)
    private String title;

    @Length(max = Bug.MAX_LENGTH_DESCRIPTION)
    private String description;
    
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
