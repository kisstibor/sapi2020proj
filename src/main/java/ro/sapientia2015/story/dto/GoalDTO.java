package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Goal;

public class GoalDTO {
	
	private Integer id; 
	
	@NotEmpty
    @Length(max = Goal.MAX_LENGTH)
    private String goal;
    
    @Length(max = Goal.MAX_LENGTH)
    private String method;
    
    @Length(max = Goal.MAX_LENGTH)
    private String metrics;
    
    private Goal.Builder builder = new Goal.Builder();
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public Goal.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Goal.Builder builder) {
		this.builder = builder;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}