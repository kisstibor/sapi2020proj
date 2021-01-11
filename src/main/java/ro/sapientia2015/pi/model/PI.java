package ro.sapientia2015.pi.model;

import java.util.List;

import javax.persistence.*;

import ro.sapientia2015.scrum.model.Scrum;


@Entity
@Table(name = "PI")
public class PI {
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_DESCRIPTION = 500;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "title", nullable = false, length = MAX_LENGTH_NAME)
	private String title;
	
	@Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
	private String description;
	
	@Column(name = "scrums")
	@OneToMany(mappedBy = "actualPi", fetch = FetchType.LAZY)
	private List<Scrum> scrums;
	
	public PI() {
	}
	
	public List<Scrum> getScrums() {
		return scrums;
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
	
	@Version
    private long version;
	
	public static Builder getBuilder(String title) {
        return new Builder(title);
    }
	
	public static class Builder {

        private PI built;
        
        public Builder(String title) {
            built = new PI();
            built.title = title;
        }
        
        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder scrums(List<Scrum> scrums) {
            built.scrums = scrums;
            return this;
        }
        
        public PI build() {
            return built;
        }
    }
}
