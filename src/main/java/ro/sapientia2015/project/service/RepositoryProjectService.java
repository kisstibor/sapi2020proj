package ro.sapientia2015.project.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Epic;
import ro.sapientia2015.project.model.Project;
import ro.sapientia2015.project.repository.EpicRepository;
import ro.sapientia2015.project.repository.ProjectRepository;

@Service
public class RepositoryProjectService implements ProjectService {

	@Resource
	private ProjectRepository repository;

	@Resource
	private EpicRepository epicRepository;

//	@Autowired
//	public void setRepository(ProjectRepository repository) {
//		this.repository = repository;
//	}
//
//	@Autowired
//	public void setEpicRepository(EpicRepository epicRepository) {
//		this.epicRepository = epicRepository;
//	}

	@Transactional
	@Override
	public Project add(ProjectDTO added) {

		Project model = added.getBuilder().setTitle(added.getTitle()).description(added.getDescription()).build();

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


	@Override
	public Project add(EpicDTO added, Long projectId) {
		try {
			System.out.println("Hmm?: " + added + " " + projectId);
			Epic epicModel = Epic.getBuilder(added.getTitle()).description(added.getDescription()).build();
			epicRepository.save(epicModel);
			Project projectModel = findById(projectId);
			projectModel.getEpics().add(epicModel);
			repository.save(projectModel);
			return projectModel;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}