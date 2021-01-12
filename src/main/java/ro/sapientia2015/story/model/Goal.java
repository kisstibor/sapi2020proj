package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name="goal")
public class Goal {
    public static final int MAX_LENGTH = 512;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "goal", nullable = false, length = MAX_LENGTH)
    private String goal;
    
    @Column(name = "method", nullable = true, length = MAX_LENGTH)
    private String method;
    
    @Column(name = "metrics", nullable = true, length = MAX_LENGTH)
    private String metrics;
    
    @Version
    private long version;
    
    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime; 
    
    public Goal() {
    	
	}
    
	public Goal(Integer id, String goal, String method, String metrics, long version, DateTime creationTime,
			DateTime modificationTime) {
		this.id = id;
		this.goal = goal;
		this.method = method;
		this.metrics = metrics;
		this.version = version;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
	}

	@PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        creationTime = now;
        modificationTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        modificationTime = DateTime.now();
    }
    
    public void update(String goal, String method, String metrics) {
        this.goal = goal;
        this.method = method;
        this.metrics = metrics;
    }
    
    public static Builder getBuilder(String goal) {
        return new Builder(goal);
    }
    
    public static class Builder {

        private Goal built;

        public Builder() {
            built = new Goal();
        }
        
        public Builder(String goal) {
            built = new Goal();
            built.goal = goal;
        }

        public Goal build() {
            return built;
        }
        
        public Builder setGoal(String goal)
        {
        	built.goal = goal;
        	return this;
        }

        public Builder setMethod(String method) {
            built.method = method;
            return this;
        }
        
        public Builder setMetrics(String metrics) {
            built.metrics = metrics;
            return this;
        }
    }

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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public DateTime getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(DateTime modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}