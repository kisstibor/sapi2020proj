package ro.sapientia2015.scrum.service;

import java.util.List;

import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.story.exception.NotFoundException;

public interface ScrumService {
	public Scrum add(ScrumDTO added);

    public Scrum deleteById(Long id) throws NotFoundException;

    public List<Scrum> findAll();

    public Scrum findById(Long id) throws NotFoundException;

    public Scrum update(ScrumDTO updated) throws NotFoundException;
}
