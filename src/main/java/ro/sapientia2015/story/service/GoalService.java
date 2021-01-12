package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Goal;

public interface GoalService {
    public Goal add(GoalDTO added);

    public Goal deleteById(Integer id) throws NotFoundException;

    public List<Goal> findAll();

    public Goal findById(Integer id) throws NotFoundException;

    public Goal update(GoalDTO updated) throws NotFoundException;

}