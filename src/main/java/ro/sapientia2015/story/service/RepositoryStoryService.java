package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.StoryRepository;

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
    public Story add(StoryDTO added) { 							// story.scrumTeam WILL NOT BE null

        Story model = Story.getBuilder(added.getTitle())
                .description(added.getDescription())
                .progress(added.getProgress())
                .build();

        log("before", model);
        
        Story ret = repository.save(model);
        
        log("after", ret);
        
        return ret;
    }
    
    @Transactional
    @Override
    public Story add(Story addedModel) {

    	log("before", addedModel);
        
    	Story ret = repository.save(addedModel);
    	
    	log("after", ret);
    	
        return ret;
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
            throw new NotFoundException("No Story entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story update(StoryDTO updated) throws NotFoundException {
        Story model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getTitle(), updated.getProgress());

        return model;
    }
    
    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story update(Story updatedModel) throws NotFoundException {
    	
    	log("BEFORE UPDATE", updatedModel);
    	
    	/* updatedModel.update(
    			updatedModel.getTitle(),
    			updatedModel.getDescription(),
    			updatedModel.getProgress(),
    			updatedModel.getScrumTeam()
    			);*/
    	//if (updatedModel.getScrumTeam() != null) { //*// uncomment
    		repository.save(updatedModel);
    	//}
        return updatedModel;
    }
    
    private void log(String smg, Story model) {
    	System.out.println(">>>>>> ("+smg+")PERSIST: Story:"
    			+ "\n | id:           " + model.getId()
    			+ "\n | title:        " + model.getTitle()
    			+ "\n | progress:     " + model.getProgress()
    			+ "\n | scrum team:   " + (model.getScrumTeam()==null?"null":model.getScrumTeam())
    	);
    	if (model.getScrumTeam() != null) {
	    	System.out.println(">>>>>> ("+smg+") PERSIST: Story.ScrumTeam:"
	    			+ "\n |  | id:          " + model.getScrumTeam().getId()
	    			+ "\n |  | name:        " + model.getScrumTeam().getName()
	    			+ "\n |  | members:     " + model.getScrumTeam().getMembers()
	    			+ "\n |  | story count: " + model.getScrumTeam().getStoryCount()
	    	);
    	}
	}
}
