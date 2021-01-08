package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @author Kiss Tibor
 */
@Entity
@Table(name="story")
public class Story {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;
    
    @Column(name = "status_modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime statusModificationTime;
    
    @Column(name = "due_date", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dueDate;
    
    @Column(name = "todo_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDurationAsString")
    private Duration todoStatusTime;
    
    @Column(name = "progress_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDurationAsString")
    private Duration progressStatusTime;
    
    @Column(name = "testing_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDurationAsString")
    private Duration testingStatusTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;
    
    @Column(name = "status", nullable = false)
    private StoryStatus status;

    @Version
    private long version;
    
    @Column(name = "comment")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "story", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Story() {

    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }
    
    public String getStatusModificationTime() {
        return statusModificationTime.toString(DateTimeFormat.forPattern("dd MMMM yyyy, HH:mm"));
    }
    
    public String getDueDate() {
        return dueDate.toString(DateTimeFormat.forPattern("dd MMMM yyyy, HH:mm"));
    }
    
    public DateTime getDueDateAsDateTime() {
        return dueDate;
    }
    
   /* public void setDueDate(String date) {
    	this.dueDate = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
    }*/
    
    /*
    public void setDueDate(DateTime date) {
    	this.dueDate = date;
    }*/
    
    public String getTodoStatusTime() {
    	PeriodFormatter formatter = new PeriodFormatterBuilder()
    		     .appendDays()
    		     .appendSuffix("d")
    		     .appendHours()
    		     .appendSuffix("h")
    		     .appendMinutes()
    		     .appendSuffix("m")
    		     .appendSeconds()
    		     .appendSuffix("s")
    		     .toFormatter();
        return formatter.print(todoStatusTime.toPeriod());
    }
    
    public String getProgressStatusTime() {
    	PeriodFormatter formatter = new PeriodFormatterBuilder()
    		     .appendDays()
    		     .appendSuffix("d")
    		     .appendHours()
    		     .appendSuffix("h")
    		     .appendMinutes()
    		     .appendSuffix("m")
    		     .appendSeconds()
    		     .appendSuffix("s")
    		     .toFormatter();
        return formatter.print(progressStatusTime.toPeriod());
    }
    
    public String getTestingStatusTime() {
    	PeriodFormatter formatter = new PeriodFormatterBuilder()
    		     .appendDays()
    		     .appendSuffix("d")
    		     .appendHours()
    		     .appendSuffix("h")
    		     .appendMinutes()
    		     .appendSuffix("m")
    		     .appendSeconds()
    		     .appendSuffix("s")
    		     .toFormatter();
        return formatter.print(testingStatusTime.toPeriod());
    }

    public String getTitle() {
        return title;
    }

    public long getVersion() {
        return version;
    }
    
    public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public boolean addComment(Comment comment) {
		return this.comments.add(comment);
	}
	
	public StoryStatus getStatus() {
        return status;
    }
	
	public void setStatus(StoryStatus newStatus) {////////////////////
		Duration diff = new Duration(statusModificationTime,DateTime.now());
		switch(status) {
			case TODO:
				todoStatusTime = todoStatusTime.withDurationAdded(diff, 1);
				break;
			case INPROGRESS:
				progressStatusTime = progressStatusTime.withDurationAdded(diff, 1);
				break;
			case UNDERTESTING:
				testingStatusTime = testingStatusTime.withDurationAdded(diff, 1);
				break;
			case DONE:
				return;
		}
		statusModificationTime = DateTime.now();
        status = newStatus;
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

    public void update(String description, String title) {
        this.description = description;
        this.title = title;
    }

    public static class Builder {

        private Story built;

        public Builder(String title) {
            built = new Story();
            built.title = title;
            built.comments = new ArrayList<Comment>();
            built.status = StoryStatus.TODO;
            //built.statusModificationTime = DateTime.now();
            built.todoStatusTime = Duration.ZERO;
            built.progressStatusTime = Duration.ZERO;
            built.testingStatusTime = Duration.ZERO;
        }

        public Story build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder dueDate(DateTime date) {
            built.dueDate = date;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
