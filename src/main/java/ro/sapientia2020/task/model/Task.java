/**
 * 
 */
package ro.sapientia2020.task.model;

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

/**
 * @author Palma Rozalia Osztian
 *
 */
@Entity
@Table(name="task")
public class Task {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;
    public static final int MAX_LENGTH_LABEL = 100;
    public static final int MAX_LENGTH_USER_TYPE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;
    
    @Column(name = "label", nullable = false, length = MAX_LENGTH_LABEL)
    private String label;

    @Column(name = "userType", length = MAX_LENGTH_USER_TYPE)
    private String userType;
    
    @Column(name = "priority")
    private String priority;
    
    @Version
    private long version;

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

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public String getTitle() {
        return title;
    }

    public long getVersion() {
        return version;
    }

    public String getLabel() {
		return label;
	}

	public String getUserType() {
		return userType;
	}

	public String getPriority() {
		return priority;
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

    public void update(String title, String description, String priority, String userType, String label) {
        this.description = description;
        this.title = title;
        this.priority = priority;
        this.userType = userType;
        this.label = label;
    }

    public static class Builder {

        private Task builtTask;

        public Builder(String title) {
            builtTask = new Task();
            builtTask.title = title;
        }

        public Task build() {
            return builtTask;
        }

        public Builder description(String description) {
            builtTask.description = description;
            return this;
        }
        
        public Builder label(String label) {
            builtTask.label = label;
            return this;
        }
        
        public Builder priority (String priority) {
            builtTask.priority = priority;
            return this;
        }
        
        public Builder userType (String userType) {
            builtTask.userType = userType;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

