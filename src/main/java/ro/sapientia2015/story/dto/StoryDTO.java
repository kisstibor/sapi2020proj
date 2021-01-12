package ro.sapientia2015.story.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.story.model.Story;

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

    private Scrum assignedTeam;
    
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

    public Scrum getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(Scrum scrum) {
        this.assignedTeam = scrum;
    }
    
    private List<Scrum> allScrums;
    
    
    public List<Scrum> getAllScrums() {
		return allScrums;
	}

	public void setAllScrums(List<Scrum> allScrums) {
		this.allScrums = allScrums;
	}
	
	private String selectedScrum;

	public String getSelectedScrum() {
		return selectedScrum;
	}

	public void setSelectedScrum(String selectedScrum) {
		this.selectedScrum = selectedScrum;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
