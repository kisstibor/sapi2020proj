package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;

public interface BugService {

	public Bug add(BugDTO added);

    public Bug deleteById(Long id) throws NotFoundException;

    public List<Bug> findAll();

    public Bug findById(Long id) throws NotFoundException;

    public Bug update(BugDTO updated) throws NotFoundException;

}