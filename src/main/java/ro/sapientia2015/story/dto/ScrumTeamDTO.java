package ro.sapientia2015.story.dto;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import ro.sapientia2015.story.model.ScrumTeam;
import ro.sapientia2015.story.model.Story;

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
