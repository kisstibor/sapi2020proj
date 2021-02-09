package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.task.model.Task;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author Kiss Tibor
 */
@Service
public class RepositoryStoryService implements StoryService {

    @Resource
    private StoryRepository repository;

    @Transactional
    @Override
    public Story add(StoryDTO added) {

        Story model = Story.getBuilder(added.getTitle())
                .description(added.getDescription())
                .build();

        return repository.save(model);
    }
    
    @Transactional
    @Override
    public Story add(Story model) {
        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story deleteById(Long id) throws NotFoundException {
        Story deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Story> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Story findById(Long id) throws NotFoundException {
        Story found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story update(StoryDTO updated) throws NotFoundException {
        Story model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getTitle(), updated.getTasks());

        return model;
    }
    
    @Override
	public Story update(Story model) throws NotFoundException {
    	model.update(model.getDescription(),model.getTitle(), model.getTasks());
    	log("ADDTASK", model);
        return model;
	}
    
    private void log(String smg, Story model) {
		try {
			System.out.println(">>>>>> ("+smg+") PERSIST: Story:"
	    			+ "\n | id:          " + model.getId()
	    			+ "\n | desc:        " + model.getDescription()
	    			+ "\n | tasks size: " + model.getTasks().size()
	    			+ "\n | tasks:     " + (model.getTasks() != null?"":"null")
	    			//+ "\n | stories:     " + (model.getStories() != null?"null":"")
	    	);
			if (model.getTasks() != null) {
		    	for (Task t : model.getTasks()) {
		    		System.out.println(
		    		    " |  |_______________"
		    		+ "\n |  | id: " + t.getId() 
		    		+ "\n |  | title:       " + t.getTitle() 
		    		//+ "\n |  | description: " + s.getDescription() 
		    		//+ "\n |  | scrumTeam:   " + s.getScrumTeam()
		    		);
		    	}
			}
		} catch (Exception e) {
			System.out.println(">>> ERROR >>> Can't LOG:");
			e.printStackTrace();
		}
	}
}
