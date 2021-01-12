package ro.sapientia2015.scrumofscrums.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.sapientia2015.scrumteam.model.ScrumTeam;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="scrum_of_scrums")
public class ScrumOfScrums {
	
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_DESCRIPTION = 500;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "name", nullable = false, length = MAX_LENGTH_NAME)
    private String name;
	
	@Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;
	
	@Column(name = "startTime", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTime;
	
	@Column(name = "scrum_teams")
	//@OneToMany(mappedBy = "scrumOfScrums", fetch = FetchType.LAZY) 
	//@OneToMany(mappedBy = "scrumOfScrums", fetch = FetchType.EAGER)
	@OneToMany(mappedBy = "scrumOfScrums")
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<ScrumTeam> scrumTeams;
	
	@Version
    private long version;
	
	
	
	public ScrumOfScrums() {

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
	
	
	public String getDescription() {
		return description;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public List<ScrumTeam> getScrumTeams() {
		return scrumTeams;
	}
	
	public long getVersion() {
        return version;
    }
	
	//
	// builder:
	//
	
	public static class Builder {

        private ScrumOfScrums built;

        public Builder(String name) {
            built = new ScrumOfScrums();
            built.name = name;
        }

        public ScrumOfScrums build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder startTime(DateTime startTime) {
            built.startTime = startTime;
            return this;
        }
        
        public Builder scrumTeams(ArrayList<ScrumTeam> scrumTeams) {
            built.scrumTeams = scrumTeams;
            return this;
        }
        
        // To add creation date use @PrePersist annotation and:
        // creationDate = DateTime.now()
        
        // To add modification date use @PreUpdate annotation and:
        // lastModifiedDate = DateTime.now()
    }
		
}
