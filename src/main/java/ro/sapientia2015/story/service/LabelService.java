package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Label;

public interface LabelService {

    public Label add(LabelDTO added);

    public List<Label> findAll();

    public Label findById(Long id) throws NotFoundException;


}
