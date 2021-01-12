package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Log;

public interface LogService {
	 public Log add(LogDTO added);


	    public Log deleteById(Long id) throws NotFoundException;


	    public List<Log> findAll();


	    public Log findById(Long id) throws NotFoundException;


	    public Log update(LogDTO updated) throws NotFoundException;
}
