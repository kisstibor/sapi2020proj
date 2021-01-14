package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.story.model.Story;

/**
 * @author Kiss Tibor
 */
public class StoryDTO {

	private final int MIN_PROGRESS_VALUE = 0;
	private final int MAX_PROGRESS_VALUE = 100;
    private Long id;

    @Length(max = Story.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Story.MAX_LENGTH_TITLE)
    private String title;
    
    private Integer progress;

    
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

    public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress > MAX_PROGRESS_VALUE
				? MAX_PROGRESS_VALUE
				: progress < MIN_PROGRESS_VALUE
						? MIN_PROGRESS_VALUE
						: progress;
		
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
