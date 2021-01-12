package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.StoryTimeLimitDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.StoryTimeLimit;
import ro.sapientia2015.story.repository.StoryTimeLimitRepository;

@Service
public class RepositoryStoryTimeLimitService implements StoryTimeLimitService {
	
	@Resource
    private StoryTimeLimitRepository repository;

	@Transactional
	@Override
	public StoryTimeLimit add(StoryTimeLimitDTO added) {
		StoryTimeLimit model = StoryTimeLimit.getBuilder(added.getStoryId())
                .timelimit(added.getTimelimit())
                .build();

        return repository.save(model);
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public StoryTimeLimit deleteById(Long id) throws NotFoundException {
		StoryTimeLimit deleted = findById(id);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public StoryTimeLimit findById(Long id) throws NotFoundException {
		StoryTimeLimit found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
	}

	@Transactional(readOnly = true)
	@Override
	public StoryTimeLimit findByStoryId(Long id) throws NotFoundException {
		List<StoryTimeLimit> timelimits = repository.findAll();
		for (StoryTimeLimit timelimit : timelimits) {
			if(timelimit.getStoryId() == id) {
				return timelimit;
			}
		}
		return null;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public StoryTimeLimit update(StoryTimeLimitDTO updated) throws NotFoundException {
		StoryTimeLimit model = findById(updated.getId());
        model.update(updated.getTimelimit());

        return model;
	}

}
