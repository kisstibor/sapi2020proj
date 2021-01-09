package ro.sapientia2015.story.model;

import java.util.List;

import javax.persistence.*;

import jdk.internal.joptsimple.internal.Strings;
import ro.sapientia2015.story.model.Story.Builder;

@Entity
@Table(name="scrum_team")
public class ScrumTeam {

	public static final int MAX_LENGTH_NAME = 100;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "name", nullable = false, length = MAX_LENGTH_NAME)
    private String name;
	
	@Column(name = "members")
    private List<String> members;
	
	@Column(name = "stories")
    private List<Story> stories;
	
	@Version
    private long version;
	
	
	public ScrumTeam() {
		
	}
	
	public static Builder getBuilder(String title) {
        return new Builder(title);
    }
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getMembers() {
		return members;
	}
	
	public String getMembersCSV() {
		return Strings.join(members, ", ");
	}

	public List<Story> getStories() {
		return stories;
	}


	
	public static class Builder {

        private ScrumTeam built;

        public Builder(String name) {
            built = new ScrumTeam();
            built.name = name;
        }

        public ScrumTeam build() {
            return built;
        }

        public Builder members(List<String> members) {
            built.members = members;
            return this;
        }
        
        public Builder stories(List<Story> stories) {
            built.stories = stories;
            return this;
        }
        
        // To add creation date use @PrePersist annotation and:
        // creationDate = DateTime.now()
        
        // To add modification date use @PreUpdate annotation and:
        // lastModifiedDate = DateTime.now()
    }
		
}
