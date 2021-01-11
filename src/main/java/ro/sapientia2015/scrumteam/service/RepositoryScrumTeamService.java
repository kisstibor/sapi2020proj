package ro.sapientia2015.scrumteam.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.scrumteam.repository.ScrumTeamRepository;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;

@Service
public class RepositoryScrumTeamService implements ScrumTeamService {

	
	@Resource
    private ScrumTeamRepository repository;
	
	
	@Transactional
    @Override
    public ScrumTeam add(ScrumTeamDTO added) {
    	//List<String> members = Arrays.asList(added.getMembers().trim().replace(" ", "").split(","));
		//List<String> selectedStoriesTitle =  added.getSelectedStories();
			
    	ScrumTeam model = ScrumTeam.getBuilder(added.getName())
    			.stories(added.getStories())
    			.build();

    	System.out.println(">>>>>> PERSIST: ScrumTeam:"
    			+ "\n | id:          " + model.getId()
    			+ "\n | name:        " + model.getName()
    			+ "\n | members:     " + model.getMembers()
    			+ "\n | story count: " + model.getStoryCount()
    			+ "\n | stories:     " + model.getStories()
    	);
        return repository.save(model);
    }


	@Transactional(rollbackFor = {NotFoundException.class})
    @Override
	public ScrumTeam deleteById(Long id) throws NotFoundException {
		ScrumTeam deleted = findById(id);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ScrumTeam> findAll() {
		return repository.findAll();
	}

	@Override
	public ScrumTeam findById(Long id) throws NotFoundException {
		ScrumTeam found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No Scrum Team entry found with id: " + id);
        }

        return found;
	}

	@Override
	public ScrumTeam update(ScrumTeamDTO updated) throws NotFoundException {
		ScrumTeam model = findById(updated.getId());
        model.update(updated.getName(), updated.getMembers(), updated.getStories());

        return model;
	}
	
}
