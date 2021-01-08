package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

@Entity
@Table(name="comment")
public class Comment {

    public static final int MAX_LENGTH_DESCRIPTION = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "message", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String message;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @ManyToOne private Story story;

    @Version
    private long version;

    public Comment() {

    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getMessage() {
        return message;
    }

    public String getModificationTime() {
        return modificationTime.toString(DateTimeFormat.forPattern("dd MMMM yyyy, HH:mm"));
    }

    public long getVersion() {
        return version;
    }
    
    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
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

    public void update(String message) {
        this.message = message;
    }

    public static class Builder {

        private Comment built;

        public Builder() {
            built = new Comment();
        }

        public Comment build() {
            return built;
        }

        public Builder message(String message) {
            built.message = message;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
