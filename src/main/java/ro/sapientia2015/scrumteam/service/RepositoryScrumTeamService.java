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
    			.members(added.getMembers().trim())
    			.stories(added.getStories())
    			.build();

        return repository.save(model);
    }


	@Override
	public ScrumTeam deleteById(Long id) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<ScrumTeam> findAll() {
		return repository.findAll();
	}

	@Override
	public ScrumTeam findById(Long id) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public ScrumTeam update(ScrumTeamDTO updated) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}
	
}
