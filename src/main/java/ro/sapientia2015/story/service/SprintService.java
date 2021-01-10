package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;

/**
 * @author Kiss Tibor
 */
public interface SprintService {

    public Sprint add(SprintDTO added);

    public Sprint deleteById(Long id) throws NotFoundException;

    public List<Sprint> findAll();

    public Sprint findById(Long id) throws NotFoundException;

    public Sprint update(SprintDTO updated) throws NotFoundException;
}
