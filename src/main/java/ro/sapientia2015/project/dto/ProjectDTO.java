package ro.sapientia2015.project.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.project.model.Epic;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.model.Project.Builder;
import ro.sapientia2015.story.model.Sprint;


public class ProjectDTO {

    private Long id;

    @Length(max = Epic.MAX_LENGTH_DESCRIPTION)
    private String description;

    private Project.Builder builder = new Project.Builder();
    
    @NotEmpty
    @Length(max = 20)
    private String title;

    private List<Epic> epics;
    
    public ProjectDTO() {

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

    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public List<Epic> getEpics() {
		return epics;
	}

	public void setStories(List<Epic> epics) {
		this.epics = epics;
	}

	public Project.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Project.Builder builder) {
		this.builder = builder;
	}
}

