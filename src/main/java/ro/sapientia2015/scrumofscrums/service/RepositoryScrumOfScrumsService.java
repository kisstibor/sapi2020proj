package ro.sapientia2015.scrumofscrums.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.scrumofscrums.dto.ScrumOfScrumsDTO;
import ro.sapientia2015.scrumofscrums.model.ScrumOfScrums;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.scrumofscrums.repository.ScrumOfScrumsRepository;

@Service
public class RepositoryScrumOfScrumsService implements ScrumOfScrumsService {

	@Resource
    private ScrumOfScrumsRepository repository;
	
	@Transactional
	@Override
	public ScrumOfScrums add(ScrumOfScrumsDTO added) {
		
		ScrumOfScrums model = ScrumOfScrums.getBuilder(added.getName())
				.description(added.getDescription())
				.startTime(added.getStartTime())
				//.scrumTeams(added.getScrumTeam())
				.build();

        System.out.println(">>>>>> PERSIST: ScrumTeam:"
    			+ "\n | id:          " + model.getId()
    			+ "\n | name:        " + model.getName()
    			+ "\n | start time:  " + model.getStartTime()
    			+ "\n | scrum team:  " + model.getScrumTeams()
    	);
        
        return repository.save(model);
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
