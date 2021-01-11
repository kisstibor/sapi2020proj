package ro.sapientia2015.story.model;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;


@Entity
@Table(name="label")
public class Label {

    public static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @Version
    private long version;

    public Label() {

    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public DateTime getCreationTime() {
        return creationTime;
    }
	
	public void setTitle(String title) {
		this.title = title;
	}

    public String getTitle() {
        return title;
    }

	public void setVersion(long version) {
		this.version = version;
	}

    public long getVersion() {
        return version;
    }

    @PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        creationTime = now;
    }

    public static class Builder {

        private Label built;

        public Builder() {
            built = new Label();
        }
        
        public Builder setTitle(String title)
        {
        	this.built.title=title;
        	return this;
        }
        
        public Builder(String title) {
            built = new Label();
            built.title = title;
        }

        public Label build() {
            return built;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
