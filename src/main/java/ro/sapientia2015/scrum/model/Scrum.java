package ro.sapientia2015.scrum.model;

import ro.sapientia2015.pi.model.PI;
import ro.sapientia2015.story.model.Story;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "scrum")
public class Scrum {
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_MEM = 500;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "title", nullable = false, length = MAX_LENGTH_NAME)
	private String title;
	
	@Column(name = "members", nullable = false, length = MAX_LENGTH_MEM)
	private String members;
	
	@Column(name = "stories")
	@OneToMany(mappedBy = "assignedTeam", fetch = FetchType.LAZY)
	private List<Story> stories;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private PI actualPi;
	
	public PI getActualPi() {
		return actualPi;
	}

	@Version
    private long version;
	
	public Scrum() {
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getMembers() {
		return members;
	}

	public List<Story> getStories() {
		return stories;
	}

	public static Builder getBuilder(String title) {
        return new Builder(title);
    }
	
	public static class Builder {

        private Scrum built;
        
        public Builder(String title) {
            built = new Scrum();
            built.title = title;
        }

        public Builder members(String members) {
            built.members = members;
            return this;
        }
        
        public Builder actualPi(PI actualPi) {
            built.actualPi = actualPi;
            return this;
        }
        
        public Scrum build() {
            return built;
        }
    }
}
