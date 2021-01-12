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
@Table(name="log")
public class Log {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;
    public static final int MAX_LENGTH_STATUS = 100;
    public static final int MAX_LENGTH_ASSIGNTO = 100;

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
    
    @Column(name = "assignto", nullable = false, length = MAX_LENGTH_ASSIGNTO)
    private String assignTo;
    
    @Column(name = "status", nullable = false, length = MAX_LENGTH_STATUS)
    private String status;
    
    @Column(name = "doc", length = MAX_LENGTH_TITLE)
    private String doc;

    @Version
    private long version;

    public Log() {

    }

    
    public String getAssignTo() {
		return assignTo;
	}


	public String getStatus() {
		return status;
	}


	public String getDoc() {
		return doc;
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

    public void update(String description, String title,String assignTo,String status) {
        this.description = description;
        this.title = title;
        this.assignTo = assignTo;
        this.status = status;
    }

    public static class Builder {

        private Log built;

        public Builder(String title) {
            built = new Log();
            built.title = title;
        }

        public Log build() {
            return built;
        }
        public Builder description(String description) {
            built.description = description;
            return this;
        }

        public Builder assignTo(String assignTo,String status) {
            built.assignTo = assignTo;
            built.status = status;
            return this;
        }
       
        public Builder doc(String doc) {
            built.doc = doc;
            return this;
        }
       
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

