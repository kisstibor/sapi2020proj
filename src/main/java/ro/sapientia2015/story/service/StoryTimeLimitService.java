package ro.sapientia2015.story.service;

import ro.sapientia2015.story.dto.StoryTimeLimitDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.StoryTimeLimit;

public interface StoryTimeLimitService {
	
	public StoryTimeLimit add(StoryTimeLimitDTO added);

    public StoryTimeLimit deleteById(Long id) throws NotFoundException;

    public StoryTimeLimit findById(Long id) throws NotFoundException;
    
    public StoryTimeLimit findByStoryId(Long id) throws NotFoundException;

    public StoryTimeLimit update(StoryTimeLimitDTO updated) throws NotFoundException;
}
