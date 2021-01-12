package ro.sapientia2015.scrum.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.scrum.repository.ScrumRepository;
import ro.sapientia2015.story.exception.NotFoundException;

@Service
public class RepositoryScrumService implements ScrumService {
	@Resource
	private ScrumRepository scrumRepository;
	
	@Transactional
	@Override
	public Scrum add(ScrumDTO added) {
		Scrum scrum = Scrum.getBuilder(added.getTitle())
    			.members(added.getMembers().trim())
    			.build();
		return scrumRepository.save(scrum);
	}

	@Transactional
	@Override
	public Scrum add(Scrum added) {
		return scrumRepository.save(added);
	}
	
	@Override
	public Scrum deleteById(Long id) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Scrum> findAll() {
		return scrumRepository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public Scrum findById(Long id) throws NotFoundException {
		return scrumRepository.findOne(id);
	}

	@Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Scrum update(ScrumDTO updated) throws NotFoundException {
		Scrum model = findById(updated.getId());
        model.update(updated.getTitle(), updated.getMembers());
        return model;
    }
    
    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Scrum update(Scrum updated) throws NotFoundException {
        return scrumRepository.save(updated);
    }

}
