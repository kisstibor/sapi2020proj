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

import ro.sapientia2015.story.model.Story.Builder;

@Entity
@Table(name="review")
public class Review {


    public static final int MAX_LENGTH_REVIEW = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;


    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "storyid", nullable = false)
    private Long storyId;
    
    @Column(name = "review", nullable = true, length = MAX_LENGTH_REVIEW)
    private String review;

    @Version
    private long version;
    
    public Review() {
    	
    }
    
    public Review(Long id, String review, Long storyId) {
    	this.id = id;
    	this.review = review;
    	this.storyId = storyId;
    }
    
    public static Builder getBuilder(Long storyId) {
        return new Builder(storyId);
    }
    
    public static Builder getBuilder(Long storyId, String review) {
        return new Builder(storyId, review);
    }
    
    public Long getId() {
        return id;
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
    
    public Long getStoryId() {
    	return storyId;
    }
    
    public String getReview() {
    	return review;
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

    public void update(String review) {
        this.review = review;
    }
    

    public static class Builder {

        private Review built;

        public Builder(Long storyId, String review) {
            built = new Review();
            built.storyId = storyId;
            built.review = review;
        }
        
        public Builder(Long storyId) {
            built = new Review();
            built.storyId = storyId;
        }
        
        public Review build() {
            return built;
        }

		public Builder review(String review) {
			built.review = review;
			return this;
		}
		
		public Builder storyId(Long storyId) {
			built.storyId = storyId;
			return this;
		}
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    
}
