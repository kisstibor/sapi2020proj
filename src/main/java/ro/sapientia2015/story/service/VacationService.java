package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.VacationDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Vacation;

public interface VacationService {
	public Vacation add(VacationDTO added);

    public Vacation deleteById(Long id) throws NotFoundException;

    public Vacation findById(Long id) throws NotFoundException;
    
    public List<Vacation> findAll();

    public Vacation update(VacationDTO updated) throws NotFoundException;
}
