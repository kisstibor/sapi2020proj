package ro.sapientia2015.task.service;
import java.util.List;

import ro.sapientia2015.task.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.task.model.Task;

public interface TaskService  {

    public Task add(TaskDTO added);

    public Task deleteById(Long id) throws NotFoundException;

    public List<Task> findAll();

    public Task findById(Long id) throws NotFoundException;

    public Task update(TaskDTO updated) throws NotFoundException;
}
