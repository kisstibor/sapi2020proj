package ro.sapientia2015.project.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.model.Epic;
import ro.sapientia2015.project.repository.EpicRepository;
import ro.sapientia2015.story.exception.NotFoundException;

@Service
public class RepositoryEpicService implements EpicService {

	@Resource
	private EpicRepository repository;

	@Transactional
	@Override
	public Epic add(EpicDTO added) {

		Epic model = Epic.getBuilder(added.getTitle()).description(added.getDescription()).build();

		return repository.save(model);
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public Epic deleteById(Long id) throws NotFoundException  {
		Epic deleted = findById(id);
		repository.delete(deleted);
		return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Epic> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = { NotFoundException.class })
	@Override
	public Epic findById(Long id) throws NotFoundException  {
		Epic found = repository.findOne(id);
		if (found == null) {
			throw new NotFoundException("No entry found with id: " + id);
		}

		return found;
	}

}
