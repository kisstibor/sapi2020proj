package ro.sapientia2015.scrumteam.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ro.sapientia2015.scrumofscrums.model.ScrumOfScrums;
import ro.sapientia2015.story.model.Story;


@Entity
@Table(name="scrumteam")
public class ScrumTeam {

	public static final int MAX_LENGTH_NAME = 100;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "name", nullable = false, length = MAX_LENGTH_NAME)
    private String name;
	
	@Column(name = "members")
    private String members;
	
	//@JoinTable(name="scrumteam_and_stories", 
    //	joinColumns={@JoinColumn(name="id1")},
    //	inverseJoinColumns=@JoinColumn(name="id2"))
	@Column(name = "stories")
	//@OneToMany(mappedBy = "scrumTeam", fetch = FetchType.LAZY)
	//@OneToMany(mappedBy = "scrumTeam", fetch = FetchType.EAGER)
	@OneToMany(mappedBy = "scrumTeam")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Story> stories;
	
	private String storiesSeparated;
	
	private Integer storyCount;
	
	@JoinColumn(name="SCRUM_OF_SCRUMS_ID")
	@ManyToOne()
//    @ManyToOne(cascade = javax.persistence.CascadeType.PERSIST)
	//@ManyToOne(cascade = CascadeType.ALL)
	//@ManyToOne(cascade = CascadeType.REMOVE)
//	@Cascade(CascadeType.SAVE_UPDATE)
	private ScrumOfScrums scrumOfScrums;
	
	@Version
    private long version;
	
	
	
	public ScrumTeam() {
		
	}
	
	public static Builder getBuilder(String title) {
        return new Builder(title);
    }
	
	public static List<Story> filterStoriesByTitle(List<Story> stories, List<String> titles) {
		List<Story> ret = new ArrayList<Story>();
		if (titles != null) {
			for (String t : titles) {
				Story found = getStoryByTitle(stories, t);
				if (found != null) {
					ret.add(found);
				}
			}
		}
		return ret;
	}
	
	public static Story getStoryByTitle(List<Story> stories, String title) {
		for (Story s : stories) {
			if (s.getTitle().equals(title)) {
				return s;
			}
		}
		return null;
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getMembers() {
		return members;
	}

	public List<Story> getStories() {
		//if (stories != null) {
		//	storyCount = stories.size();	// Refresh
		//}
		return stories;
	}
	
	public String getStoriesSeparated() {
		return storiesSeparated;
	}
	
	public Integer getStoryCount() {
		return (stories == null) ? 0 : stories.size();
	}
	
	public ScrumOfScrums getScrumOfScrums() {
		return scrumOfScrums;
	}
	
	public void addStory(Story story) {
		stories.add(story);
		storyCount = stories.size();
	}

	private void updateStoriesCSV() {
		String titles = "";
		if (stories != null) {
			int i = 0;
			for(Story s : stories) {
				titles += s.getTitle();
				i++;
				if (i != stories.size()) {
					titles += ", ";
				}
			}
		}
		storiesSeparated = titles;
	}
	
	public void updateStory(Story story) {
		if (stories != null) {
			for (Story s : stories) {
				if (story.getId() == s.getId()) {
					stories.remove(s);
					stories.add(story);
					storyCount = stories.size();
					return;
				}
			}
		}
	}
	

    public void update(String name, String members, List<Story> stories) {
        this.name = name;
        this.members = members;
        this.stories = stories;
    }
	
	public static class Builder {

        private ScrumTeam built;

        public Builder(String name) {
            built = new ScrumTeam();
            built.name = name;
        }

        public Builder members(String members) {
            built.members = members;
            return this;
        }
        
        public Builder stories(List<Story> stories) {
            built.stories = stories;
            if (stories !=null) {
            	built.storyCount = stories.size();
            }
            built.updateStoriesCSV();
            return this;
        }
        
        public Builder scrumOfScrums(ScrumOfScrums scrumOfScrums) {
        	built.scrumOfScrums = scrumOfScrums;
            return this;
        }
        
        public ScrumTeam build() {
            return built;
        }
        
        // To add creation date use @PrePersist annotation and:
        // creationDate = DateTime.now()
        
        // To add modification date use @PreUpdate annotation and:
        // lastModifiedDate = DateTime.now()
    }
		
}
