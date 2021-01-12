package ro.sapientia2015.scrum.model;

import ro.sapientia2015.pi.model.PI;
import ro.sapientia2015.story.model.Story;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
	@OneToMany(mappedBy = "assignedTeam")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Story> stories;
	
	@JoinColumn(name="actual_pi_join")
	@ManyToOne()
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

	public void addStory(Story story) {
		stories.add(story);
	}

	public void update(String title, String members) {
		this.title = title;
		this.members = members;
	}
}
