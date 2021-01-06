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
@Table(name="user")
public class User {
	
	public static final int MAX_LENGTH_USERNAME = 255;
    public static final int MAX_LENGTH_PASSWORD = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;   

    @Column(name = "username", nullable = false, length = MAX_LENGTH_USERNAME)
    private String username;

	@Column(name = "password", nullable = false, length = MAX_LENGTH_PASSWORD)
    private String password;
	
	@Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;
	
	@Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;
    
    @Version
    private long version;
    
    public User() {
    	
    }
    
    public static Builder getBuilder(String username) {
        return new Builder(username);
    }

    public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
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
	
	public static class Builder {

        private User built;

        public Builder(String username) {
            built = new User();
            built.username = username;
        }

        public User build() {
            return built;
        }

        public Builder password(String password) {
            built.password = password;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
