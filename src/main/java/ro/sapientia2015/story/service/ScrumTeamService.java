package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.ScrumTeamDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.ScrumTeam;

public interface ScrumTeamService {
	
    public ScrumTeam add(ScrumTeamDTO added);

    public ScrumTeam deleteById(Long id) throws NotFoundException;

    public List<ScrumTeam> findAll();

    public ScrumTeam findById(Long id) throws NotFoundException;

    public ScrumTeam update(ScrumTeamDTO updated) throws NotFoundException;
}
