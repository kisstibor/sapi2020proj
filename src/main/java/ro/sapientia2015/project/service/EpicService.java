package ro.sapientia2015.project.service;

import java.util.List;

import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.model.Epic;


public interface EpicService {

    public Epic add(EpicDTO added);

    public Epic deleteById(Long id) throws NotFoundException;

    public List<Epic> findAll();

    public Epic findById(Long id) throws NotFoundException;

    
}