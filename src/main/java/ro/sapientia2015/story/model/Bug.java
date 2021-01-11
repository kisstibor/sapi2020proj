package ro.sapientia2015.story.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* @author Zsok Andrei
*/
@Entity
@Table(name="bug")
public class Bug {
	
	public static final int MAX_LENGTH_DESCRIPTION = 300;
    public static final int MAX_LENGTH_TITLE = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;
    
    public Bug() {
    }
    
    public Bug(String title, String description) {
    	
    }
    
    public Long getId() {
        return this.id;
    }
   
    public String getTitle() {
    	return this.title;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public void setDescription (String description) {
    	this.description = description;
    }
    
    public void update(String description, String title) {
        this.description = description;
        this.title = title;
    }
    
    public static Builder getBuilder(String title) {
        return new Builder(title);
    }
    
    public static class Builder {

        private Bug built;

        public Builder(String title) {
            built = new Bug();
            built.title = title;
        }

        public Bug build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
    }

}
