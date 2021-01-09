package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;


@Entity
@Table(name="daily")
public class Daily {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;
    public static final int MAX_LENGTH_DATEE = 10;
    public static final int MAX_LENGTH_DURATION = 3;

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
    
    @Column(name = "datee", nullable = false, length = MAX_LENGTH_DATEE)
    private String datee;
    
    @Column(name = "duration", nullable = false, length = MAX_LENGTH_DURATION)
    private String duration;

    @Version
    private long version;

    public Daily() {

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

    public String getDatee() {
		return datee;
	}

	public String getDuration() {
		return duration;
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

    public void update(String description, String title, String datee, String duration) {
        this.description = description;
        this.title = title;
        this.datee = datee;
        this.duration = duration;
    }

    public static class Builder {

        private Daily built;

        public Builder(String title) {
            built = new Daily();
            built.title = title;
        }

        public Daily build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder duration(String duration) {
            built.duration = duration;
            return this;
        }
        
        public Builder datee(String datee) {
            built.datee = datee;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
