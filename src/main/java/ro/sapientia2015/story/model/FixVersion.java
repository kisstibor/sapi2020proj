package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;


@Entity
@Table(name="fix_version")
public class FixVersion {
	
	public static final int MAX_LENGTH_TITLE = 100;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "name", nullable = false, length = MAX_LENGTH_TITLE)
    private String name;
	
	@Version
    private long version;
	
	public FixVersion() {

    }

    public static Builder getBuilder(String name) {
        return new Builder(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void update(String name) {
        this.name = name;
    }
    
    public static class Builder {

        private FixVersion built;

        public Builder(String name) {
            built = new FixVersion();
            built.name = name;
        }

        public FixVersion build() {
            return built;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
