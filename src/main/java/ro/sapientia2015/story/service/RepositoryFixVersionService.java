package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.FixVersionRepository;

@Service
public class RepositoryFixVersionService implements FixVersionService{

	@Resource
    private FixVersionRepository repository;
	
	@Transactional
    @Override
	public FixVersion add(FixVersionDTO added) {
		FixVersion model = FixVersion.getBuilder(added.getName())
                .build();

        return repository.save(model);
	}

	@Transactional(rollbackFor = {NotFoundException.class})
    @Override
	public FixVersion deleteById(Long id) throws NotFoundException {
		FixVersion deleted = findById(id);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true)
    @Override
	public List<FixVersion> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
	public FixVersion findById(Long id) throws NotFoundException {
		FixVersion found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
    @Override
	public FixVersion update(FixVersionDTO updated) throws NotFoundException {
		FixVersion model = findById(updated.getId());
        model.update(updated.getName());

        return model;
	}

}
