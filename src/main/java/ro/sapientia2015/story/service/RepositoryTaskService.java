package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Task;
import ro.sapientia2015.story.repository.TaskRepository;

@Service
public class RepositoryTaskService implements TaskService{

	
	 @Resource
	 private TaskRepository repository;


	    @Transactional
	    @Override
	    public Task add(TaskDTO added) {

	    	Task model = Task.getBuilder(added.getTitle())
	                .description(added.getDescription())
	                .state(added.getState())
	                .build();

	        return repository.save(model);
	    }

	    @Transactional(rollbackFor = {NotFoundException.class})
	    @Override
	    public Task deleteById(Long id) throws NotFoundException {
	    	Task deleted = findById(id);
	        repository.delete(deleted);
	        return deleted;
	    }

	    @Transactional(readOnly = true)
	    @Override
	    public List<Task> findAll() {
	       return repository.findAll();
	    }

	    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	    @Override
	    public Task findById(Long id) throws NotFoundException {
	    	Task found = repository.findOne(id);
	        if (found == null) {
	            throw new NotFoundException("No entry found with id: " + id);
	        }

	        return found;
	    }

		@Override
		@Transactional(rollbackFor = {NotFoundException.class})
		public Task update(TaskDTO updated) throws NotFoundException {
		    
		   Task model = findById(updated.getId());
		   model.update(updated.getState());

		   return model;
		}



}
