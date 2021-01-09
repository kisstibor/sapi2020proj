package ro.sapientia2015.story.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.ScrumTeamDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.ScrumTeam;
import ro.sapientia2015.story.repository.ScrumTeamRepository;

public class RepositoryScrumTeamService implements ScrumTeamService {

	
	@Resource
    private ScrumTeamRepository repository;
	
	@Transactional
    @Override
    public ScrumTeam add(ScrumTeamDTO added) {
    	List<String> members = Arrays.asList(added.getMembers().trim().replace(" ", "").split(","));

    	ScrumTeam model = ScrumTeam.getBuilder(added.getName())
    			.members(members)
    			.build();

    	// TODO >>>>>> set stories

        return repository.save(model);
    }


	@Override
	public ScrumTeam deleteById(Long id) throws NotFoundException {
		// TODO >>>>>> Auto-generated method stub
		return null;
	}

	@Override
	public List<ScrumTeam> findAll() {
		// TODO >>>>>> Auto-generated method stub
		return null;
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
