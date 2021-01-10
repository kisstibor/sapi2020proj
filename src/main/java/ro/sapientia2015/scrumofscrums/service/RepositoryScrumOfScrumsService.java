package ro.sapientia2015.scrumofscrums.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.scrumofscrums.dto.ScrumOfScrumsDTO;
import ro.sapientia2015.scrumofscrums.model.ScrumOfScrums;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.scrumofscrums.repository.ScrumOfScrumsRepository;

public class RepositoryScrumOfScrumsService implements ScrumOfScrumsService {

	@Resource
    private ScrumOfScrumsRepository repository;
	
	@Transactional
	@Override
	public ScrumOfScrums add(ScrumOfScrumsDTO added) {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public ScrumOfScrums deleteById(Long id) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public List<ScrumOfScrums> findAll() {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public ScrumOfScrums findById(Long id) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public ScrumOfScrums update(ScrumOfScrumsDTO updated) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

}
