package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Story;

public class PriorityDTO {
	private Long id;


    @NotEmpty
    @Length(max = Story.MAX_LENGTH_TITLE)
    private String title;
    


    public PriorityDTO() {

    }

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


	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
