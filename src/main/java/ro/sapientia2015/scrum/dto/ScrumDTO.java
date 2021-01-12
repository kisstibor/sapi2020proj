package ro.sapientia2015.scrum.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import ro.sapientia2015.scrum.model.Scrum;

public class ScrumDTO {
    private Long id;
	
    @Length(max = Scrum.MAX_LENGTH_NAME)
	private String title;
	
	private String members;

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

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
