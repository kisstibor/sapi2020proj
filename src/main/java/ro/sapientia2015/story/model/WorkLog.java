package ro.sapientia2015.story.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.sapientia2015.story.model.Story.Builder;

@Entity
@Table(name="worklog")
public class WorkLog {
	public static final int MAX_LENGTH_DESCRIPTION = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "created_at", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdAt;

	@Column(name = "modified_at", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedAt;
    
    @Column(name = "story_id", nullable = false)
    private Long story_id;

	@Column(name = "story_title", nullable = false)
    private String story_title;
    
    @Column(name = "logged_at", nullable = false)
    private LocalDate logged_at;
    
    @Column(name = "started_at", nullable = false)
    private LocalTime started_at;
    
    @Column(name = "ended_at", nullable = false)
    private LocalTime ended_at;
    
    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Version
    private long version;
    
    public Long getStory_id() {
		return story_id;
	}

	public void setStory_id(Long story_id) {
		this.story_id = story_id;
	}

	public String getStory_title() {
		return story_title;
	}

	public void setStory_title(String story_title) {
		this.story_title = story_title;
	}

	public LocalDate getLogged_at() {
		return logged_at;
	}

	public void setLogged_at(LocalDate logged_at) {
		this.logged_at = logged_at;
	}

	public LocalTime getStarted_at() {
		return started_at;
	}

	public void setStarted_at(LocalTime started_at) {
		this.started_at = started_at;
	}

	public LocalTime getEnded_at() {
		return ended_at;
	}

	public void setEnded_at(LocalTime ended_at) {
		this.ended_at = ended_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public WorkLog() {

    }

    public static Builder getBuilder(Long story_id, String story_title) {
        return new Builder(story_id, story_title);
    }

    public Long getId() {
        return id;
    }
    
    public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}


    @PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        createdAt = now;
        modifiedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
    	modifiedAt = DateTime.now();
    }

    public void update(Long story_id, String story_title, LocalDate logged_at, LocalTime started_at, LocalTime ended_at, String description) {
        this.story_id = story_id;
        this.story_title = story_title;
        this.logged_at = logged_at;
        this.started_at = started_at;
        this.ended_at = ended_at;
        this.description = description;
    }

    public static class Builder {

        private WorkLog builtWorkLog;

        public Builder(Long story_id, String story_title) {
        	builtWorkLog = new WorkLog();
        	builtWorkLog.story_id = story_id;
        	builtWorkLog.story_title = story_title;
        }

        public WorkLog build() {
            return builtWorkLog;
        }

        public Builder story_id(Long story_id) {
        	builtWorkLog.story_id = story_id;
            return this;
        }
        
        public Builder story_title(String story_title) {
        	builtWorkLog.story_title = story_title;
            return this;
        }
        
        public Builder logged_at(LocalDate logged_at) {
        	builtWorkLog.logged_at = logged_at;
            return this;
        }
        
        public Builder started_at(LocalTime started_at) {
        	builtWorkLog.started_at = started_at;
            return this;
        }
        
        
        public Builder ended_at(LocalTime ended_at) {
        	builtWorkLog.ended_at = ended_at;
            return this;
        }
        
        public Builder description(String description) {
        	builtWorkLog.description = description;
            return this;
        }
        
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
