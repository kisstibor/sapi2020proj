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
	@Table(name="task")
public class Task {

	    public static final int MAX_LENGTH_DESCRIPTION = 500;
	    public static final int MAX_LENGTH_TITLE = 100;

	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column(name = "creation_time", nullable = false)
	    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	    private DateTime creationTime;

	    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
	    private String description;

	    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
	    private String title;
	    
	    @Column(name = "state", length = MAX_LENGTH_TITLE)
	    private String state;
	    
	    public Task() {

	    }

	    public static Builder getBuilder(String title) {
	        return new Builder(title);
	    }

	    public Long getId() {
	        return id;
	    }

	    public DateTime getCreationTime() {
	        return creationTime;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public String getTitle() {
	        return title;
	    }
	    
	    public String getState() {
	    	return state;
	    }
	    
	    public void setState(String state)
	    {
	    	this.state = state;
	    }


	    @PrePersist
	    public void prePersist() {
	        DateTime now = DateTime.now();
	        creationTime = now;
	    }


	    public void update(String state) {
	        this.state = state;
	    }

	    public static class Builder {

	        private Task built;

	        public Builder(String title) {
	            built = new Task();
	            built.title = title;
	        }

	        public Task build() {
	            return built;
	        }

	        public Builder description(String description) {
	            built.description = description;
	            return this;
	        }
	        
	        public Builder id(Long id) {
	            built.id = id;
	            return this;
	        }
	        
	        public Builder state(String state) {
	            built.state = state;
	            return this;
	        }
	    }

	    @Override
	    public String toString() {
	        return ToStringBuilder.reflectionToString(this);
	    }
}
