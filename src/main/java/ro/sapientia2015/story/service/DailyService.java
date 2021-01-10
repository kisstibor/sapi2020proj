package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Daily;


public interface DailyService {

    public Daily add(DailyDTO added);

  
    public Daily deleteById(Long id) throws NotFoundException;

  
    public List<Daily> findAll();


    public Daily findById(Long id) throws NotFoundException;


    public Daily update(DailyDTO updated) throws NotFoundException;
}
