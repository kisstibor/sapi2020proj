package ro.sapientia2015.story.dto;

import java.util.List;

import ro.sapientia2015.story.model.Story;

public class StoryListDTO {
	
	private List<Story> stories;
	
	private String query;
	
	public StoryListDTO() {
		
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
