package ro.sapientia2015.scrumteam.dto;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import ro.sapientia2015.scrumteam.model.ScrumTeam;

public class ScrumTeamDTO {

    private Long id;
	
    @Length(max = ScrumTeam.MAX_LENGTH_NAME)
	private String name;
	
    private String members;
    
	//private ArrayList<Story> stories;
	
	
	
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

/*

	public ArrayList<Story> getStories() {
		return stories;
	}
	



	public void setStories(ArrayList<Story> stories) {
		this.stories = stories;
	}

*/


	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
