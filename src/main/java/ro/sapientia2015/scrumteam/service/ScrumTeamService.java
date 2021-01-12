package ro.sapientia2015.scrumteam.service;

import java.util.List;

import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.story.exception.NotFoundException;

public interface ScrumTeamService {
	
    public ScrumTeam add(ScrumTeamDTO added);
    public ScrumTeam add(ScrumTeam addedModel);

    public ScrumTeam deleteById(Long id) throws NotFoundException;

    public List<ScrumTeam> findAll();

    public ScrumTeam findById(Long id) throws NotFoundException;

    public ScrumTeam update(ScrumTeamDTO updated) throws NotFoundException;
    public ScrumTeam update(ScrumTeam updatedModel) throws NotFoundException;
}
