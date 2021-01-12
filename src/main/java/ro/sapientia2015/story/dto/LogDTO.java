package ro.sapientia2015.story.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Log;

public class LogDTO {
	 private Long id;

	    @Length(max = Log.MAX_LENGTH_DESCRIPTION)
	    private String description;

	    @NotEmpty
	    @Length(max = Log.MAX_LENGTH_TITLE)
	    private String title;
	    
	    @NotEmpty
	    @Length(max = Log.MAX_LENGTH_STATUS)
	    private String status;
	    
	    @NotEmpty
	    @Length(max = Log.MAX_LENGTH_ASSIGNTO)
	    private String assignTo;
	    
	    
	    private String doc;
	    

	    public LogDTO() {

	    }

	    
	    public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public String getAssignTo() {
			return assignTo;
		}


		public void setAssignTo(String assignTo) {
			this.assignTo = assignTo;
		}


		public String getDoc() {
			return doc;
		}


		public void setDoc(String doc) {
			this.doc = doc;
		}


		public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    @Override
	    public String toString() {
	        return ToStringBuilder.reflectionToString(this);
	    }
	}
