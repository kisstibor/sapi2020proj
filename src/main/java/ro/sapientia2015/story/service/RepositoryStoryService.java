package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.PriorityRepository;
import ro.sapientia2015.story.repository.StoryRepository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kapas Krisztina
 */
@Service
public class RepositoryStoryService implements StoryService {

    @Resource
    private StoryRepository repository;
    
    @Resource
    private PriorityRepository priorityRepository;

    @Transactional
    @Override
    public Story add(StoryDTO added) {    	
    	Priority p = null;
    	
    	if ( added.getPriorityId() != null )
    	{
    		p = priorityRepository.findOne(added.getPriorityId());
    	}

        Story model = Story.getBuilder(added.getTitle())
                .description(added.getDescription())
                .priority(p)
                .build();

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
    
    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public List<Story> findByPriorityId(Long id){
    	
        List<Story> stories  		= repository.findAll();
        List<Story> storyResultList = new ArrayList();
        
        for( Story story : stories )
        {
        	if( story.getPriority().getId() == id )
        	{
        		storyResultList.add( story );
        	}
        }

        return storyResultList;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story update(StoryDTO updated) throws NotFoundException {
        Story model = findById(updated.getId());
        
        Priority p = null;
    	
    	if ( updated.getPriorityId() != null )
    	{
    		p = priorityRepository.findOne(updated.getPriorityId());
    	}
        
        model.update(updated.getDescription(), updated.getTitle(), p );

        return model;
    }
}
