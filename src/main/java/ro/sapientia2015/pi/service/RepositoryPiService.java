package ro.sapientia2015.pi.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.pi.dto.PIDTO;
import ro.sapientia2015.pi.model.PI;
import ro.sapientia2015.pi.repository.PIRepository;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.story.exception.NotFoundException;

public class RepositoryPiService implements PIService {

	@Resource
	private PIRepository piRepository;
	
	@Transactional
	@Override
	public PI add(PIDTO added) {
		PI scrum = PI.getBuilder(added.getTitle())
    			.scrums(added.getScrums())
    			.build();
		return piRepository.save(scrum);
	}

	@Override
	public PI deleteById(Long id) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PI> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PI findById(Long id) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PI update(PIDTO updated) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
