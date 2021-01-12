package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * @author Kiss Tibor
 */
@Entity
@Table(name="user")
public class User {

    public static final int MAX_LENGTH_TYPE = 500;
    public static final int MAX_LENGTH_NAME = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "type", nullable = true, length = MAX_LENGTH_TYPE)
    private String type;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "name", nullable = false, length = MAX_LENGTH_NAME)
    private String name;

    @Version
    private long version;

    public User() {

    }

    public static Builder getBuilder(String name) {
        return new Builder(name);
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getType() {
        return type;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public String getName() {
        return name;
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

    public void update(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static class Builder {

        private User built;

        public Builder(String name) {
            built = new User();
            built.name = name;
        }

        public User build() {
            return built;
        }

        public Builder type(String type) {
            built.type = type;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
