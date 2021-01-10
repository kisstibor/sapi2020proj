package ro.sapientia2015.story.model;

import java.util.Date;

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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="story_timelimit")
public class StoryTimeLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;
    
    @Column(name = "timelimit", nullable = false)
    private String timelimit;
    
    @Column(name = "storyId", nullable = false)
    private Long storyId;

    @Version
    private long version;

    public StoryTimeLimit() {

    }

    public static Builder getBuilder(Long storyId) {
        return new Builder(storyId);
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public String getTimelimit() {
        return timelimit;
    }
    
    public Long getStoryId() {
        return storyId;
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

    public void update(String timelimit) {
        this.timelimit = timelimit;
    }

    public static class Builder {

        private StoryTimeLimit built;

        public Builder(Long storyId) {
            built = new StoryTimeLimit();
            built.storyId = storyId;
        }

        public StoryTimeLimit build() {
            return built;
        }

        public Builder timelimit(String timelimit) {
            built.timelimit = timelimit;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

