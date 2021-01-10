package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.repository.BugRepository;

/**
 * @author Zsok Andrei
 */
@Service
public class RepositoryBugService implements BugService {
	
	@Resource
	private BugRepository repository;

	@Override
	public Bug add(BugDTO bug) {
		Bug newBug = Bug.getBuilder(bug.getTitle())
                .description(bug.getDescription())
                .build();
		return repository.save(newBug);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Bug> findAll() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Bug findById(Long id) throws NotFoundException {
        Bug found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Bug update(BugDTO bug) throws NotFoundException {
		Bug model = findById(bug.getId());
        model.update(bug.getDescription(), bug.getTitle());

        return model;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Bug delete(Long id) throws NotFoundException {
		Bug bug = findById(id);
        repository.delete(bug);
        return bug;
	}

}
