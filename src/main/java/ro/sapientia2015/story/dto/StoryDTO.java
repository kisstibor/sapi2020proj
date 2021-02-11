package ro.sapientia2015.story.dto;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Story;

/**
 * @author Kiss Tibor
 */
public class StoryDTO {

    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;
    
    @Length(max = Story.MAX_LENGTH_COMMENT)
    private String comment;

    @NotEmpty
    @Length(max = Story.MAX_LENGTH_TITLE)
    private String title;
    
    @Length(max = Story.MAX_LENGTH_TEAM)
    private String team;
    
    @NotNull
    private int points;


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
    
    public String getComment() {
        return comment;
    }
    
    public String getTeam() {
    	return team;
    }
    
    public int getPoints() {
    	return points;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTeam(String team) {
		this.team = team;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
