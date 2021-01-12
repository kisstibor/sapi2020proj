package ro.sapientia2015.story.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class StoryTimeLimitDTO {
	private Long id;

    private String timelimit;
    
    private Long storyId;

    public StoryTimeLimitDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(String timelimit) {
        this.timelimit = timelimit;
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
