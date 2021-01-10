package ro.sapientia2015.pi.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import ro.sapientia2015.pi.model.PI;
import ro.sapientia2015.scrum.model.Scrum;

public class PIDTO {
    private Long id;
	
    @Length(max = PI.MAX_LENGTH_NAME)
	private String title;
	
    @Length(max = PI.MAX_LENGTH_DESCRIPTION)
	private String description;
	
	private List<Scrum> scrums;
	
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public List<Scrum> getScrums() {
		return scrums;
	}



	public void setScrums(List<Scrum> scrums) {
		this.scrums = scrums;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
