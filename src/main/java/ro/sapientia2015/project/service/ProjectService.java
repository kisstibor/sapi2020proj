package ro.sapientia2015.project.service;

import java.util.List;

import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.dto.ProjectDTO;
import ro.sapientia2015.project.model.Project;


public interface ProjectService {

    public Project add(ProjectDTO added);

    public Project add(EpicDTO added,Long projectId);
    
    public Project deleteById(Long id) throws NotFoundException;

    public List<Project> findAll();

    public Project findById(Long id) throws NotFoundException;
    
}