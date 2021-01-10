package ro.sapientia2015.project.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.repository.ProjectRepository;
import ro.sapientia2015.story.exception.NotFoundException;

@Service
public class RepositoryProjectService implements ProjectService {

	@Resource
	private ProjectRepository repository;

	@Transactional
	@Override
	public Project add(ProjectDTO added) {
 		Project model = Project.getBuilder(added.getName()).productOwner(added.getProductOwner())
				.scrumMaster(added.getScrumMaster()).members(added.getMembers()).build();
		return repository.save(model);
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public Project deleteById(Long id) throws NotFoundException {
		Project deleted = findById(id);
		repository.delete(deleted);
		return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Project> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = { NotFoundException.class })
	@Override
	public Project findById(Long id) throws NotFoundException {
		Project found = repository.findOne(id);
		if (found == null) {
			throw new NotFoundException("No entry found with id: " + id);
		}
		return found;
	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public Project update(ProjectDTO updated) throws NotFoundException {
		Project model = findById(updated.getId());
		model.update(updated.getName(), updated.getProductOwner(), updated.getScrumMaster(), updated.getMembers());
		return model;
	}
}
