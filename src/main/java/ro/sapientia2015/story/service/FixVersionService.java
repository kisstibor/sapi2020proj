package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.FixVersion;

public interface FixVersionService {

    public FixVersion add(FixVersionDTO added);

    public FixVersion deleteById(Long id) throws NotFoundException;

    public List<FixVersion> findAll();

    public FixVersion findById(Long id) throws NotFoundException;

    public FixVersion update(FixVersionDTO updated) throws NotFoundException;
}
