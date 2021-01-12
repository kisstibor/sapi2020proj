package ro.sapientia2015.story.service;

import java.time.LocalTime;
import java.util.List;

import ro.sapientia2015.story.dto.WorkLogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.WorkLog;

public interface WorkLogService {
	public WorkLog add(WorkLogDTO added);

    public WorkLog deleteById(Long id) throws NotFoundException;

    public List<WorkLog> findAll();
    
    public List<WorkLog> findByLoggedDate(String date);

    public WorkLog findById(Long id) throws NotFoundException;

    public WorkLog update(WorkLogDTO updated) throws NotFoundException;
    
    public boolean checkIfLoggedEarlier(String date, LocalTime started_time, LocalTime ended_time);
}
