package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;

/**
 * @author Zsok Andrei
 */
public interface BugService {
	
	public Bug add(BugDTO bug);
	
	public List<Bug> findAll();
	
	public Bug findById(Long id) throws NotFoundException;
	
	public Bug update(BugDTO bug) throws NotFoundException;
	
	public Bug delete(Long id) throws NotFoundException;

}
