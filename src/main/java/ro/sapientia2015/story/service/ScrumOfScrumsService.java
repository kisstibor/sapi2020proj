package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.ScrumOfScrumsDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.ScrumOfScrums;;

public interface ScrumOfScrumsService {
	
	public ScrumOfScrums add(ScrumOfScrumsDTO added);

    public ScrumOfScrums deleteById(Long id) throws NotFoundException;

    public List<ScrumOfScrums> findAll();

    public ScrumOfScrums findById(Long id) throws NotFoundException;

    public ScrumOfScrums update(ScrumOfScrumsDTO updated) throws NotFoundException;
}
