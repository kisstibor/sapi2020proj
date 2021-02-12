package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="bug")
public class Bug {

	public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;
    public static final int MAX_LENGTH_STATUS = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;
    
    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;
    
    @Column(name= "status", nullable = true, length = MAX_LENGTH_STATUS)
    private String status;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Version
    private long version;

    public Bug() {

    }
    
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getStatus() {
    	return status;
    }
    
    public DateTime getCreationTime() {
        return creationTime;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public long getVersion() {
        return version;
    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
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

    public void update(String title, String description, String status) {
    	this.title = title;
        this.description = description;
        this.status = status;
    }

    public static class Builder {

        private Bug built;

        public Builder(String title) {
            built = new Bug();
            built.title = title;
        }

        public Bug build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder status(String status) {
        	built.status = status;
        	return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}