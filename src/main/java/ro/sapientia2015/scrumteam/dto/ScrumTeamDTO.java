package ro.sapientia2015.scrumteam.dto;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.story.model.Story;

public class ScrumTeamDTO {

    private Long id;
	
    @Length(max = ScrumTeam.MAX_LENGTH_NAME)
	private String name;
	
    private String members;
    
	private List<Story> stories;
	
	private List<String> selectedStories;
	
	
	public ScrumTeamDTO() {
		
	}
	
	public void setFrom(ScrumTeam from) {
		this.id = from.getId();
		this.name = from.getName();
		this.members = from.getMembers();
		this.stories = from.getStories();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMembers() {
		return members;
	}

	public List<String> getMembersList() {
		return Arrays.asList(members.trim().replace(" ", "").split(","));
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public List<String> getSelectedStories() {
		return selectedStories;
	}

	public void setSelectedStories(List<String> selectedStories) {
		this.selectedStories = selectedStories;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
