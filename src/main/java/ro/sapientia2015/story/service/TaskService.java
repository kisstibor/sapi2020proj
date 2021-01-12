package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Task;

public interface TaskService {

	public Task add(TaskDTO dto);
	
	
    public Task deleteById(Long id) throws NotFoundException;

  
    public List<Task> findAll();

    
    public Task findById(Long id) throws NotFoundException;

    
    public Task update(TaskDTO updated) throws NotFoundException;

}
