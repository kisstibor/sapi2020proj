package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Goal;
import ro.sapientia2015.story.repository.GoalRepository;

@Service
public class RepositoryGoalService implements GoalService {
	
	@Resource
	private GoalRepository repository;

	@Transactional
	@Override
	public Goal add(GoalDTO added) {
		Goal model = added.getBuilder()
				.setGoal(added.getGoal())
				.setMethod(added.getMethod())
				.setMetrics(added.getMetrics())
                .build();

        return repository.save(model);
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Goal deleteById(Integer id) throws NotFoundException {
        Goal deleted = findById(id);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Goal> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public Goal findById(Integer id) throws NotFoundException {
        Goal found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Goal update(GoalDTO updated) throws NotFoundException {
		Goal model = findById(updated.getId());
        model.update(updated.getGoal(), updated.getMethod(), updated.getMetrics());

        return model;
	}

}
