package ro.sapientia2020.task.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2020.task.model.Task;


public class TaskDTO {

    private Long id;

    @Length(max = Task.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Length(max = Task.MAX_LENGTH_TITLE)
    private String title;
    
    @NotEmpty
    @Length(max = Task.MAX_LENGTH_LABEL)
    private String label;
    
   
    private String priority;
    
    @Length(max = Task.MAX_LENGTH_USER_TYPE)
    private String userType;

    public TaskDTO() {

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

    public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPriority() {
		return priority;
	}

	public void setPrority(String priority) {
		this.priority = priority;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
