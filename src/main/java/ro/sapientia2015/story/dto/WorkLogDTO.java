package ro.sapientia2015.story.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.WorkLog;
import ro.sapientia2015.story.model.WorkLog.Builder;

public class WorkLogDTO {

    private Long id;

    private Long story_id;

    private String story_title;
    
    private String logged_at;
    
    private LocalDate logged_at_date;


	private String started_at;
	
	private LocalTime started_at_date;

	private String ended_at;
	
    private LocalTime ended_at_date;
    
    @Length(max = WorkLog.MAX_LENGTH_DESCRIPTION)
    private String description;
    
    public WorkLogDTO() {

    }
    
    public Long getStory_id() {
		return story_id;
	}

	public void setStory_id(long story_id) {
		this.story_id = story_id;
	}

	public String getStory_title() {
		return story_title;
	}

	public void setStory_title(String story_title) {
		this.story_title = story_title;
	}

	public String getLogged_at() {
		return logged_at;
	}

	public void setLogged_at(String logged_at) {
		this.logged_at = logged_at;
	}

	public String getStarted_at() {
		return started_at;
	}

	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}

	public String getEnded_at() {
		return ended_at;
	}

	public void setEnded_at(String ended_at) {
		this.ended_at = ended_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getLogged_at_date() {
		return logged_at_date;
	}

	public void setLogged_at_date(LocalDate logged_at_date) {
		this.logged_at_date = logged_at_date;
	}
	
	public LocalTime getStarted_at_date() {
		return started_at_date;
	}

	public void setStarted_at_date(LocalTime started_at_date) {
		this.started_at_date = started_at_date;
	}

	public LocalTime getEnded_at_date() {
		return ended_at_date;
	}

	public void setEnded_at_date(LocalTime ended_at_date) {
		this.ended_at_date = ended_at_date;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
