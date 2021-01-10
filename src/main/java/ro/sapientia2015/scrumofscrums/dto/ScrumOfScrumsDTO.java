package ro.sapientia2015.scrumofscrums.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class ScrumOfScrumsDTO {


    private String name;
    
    private String description;
    
    private DateTime startTime;
	
    // TODO >>>>>>
    //private ArrayList<ScrumTeam> scrumTeams;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
