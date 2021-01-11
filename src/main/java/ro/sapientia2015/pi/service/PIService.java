package ro.sapientia2015.pi.service;

import java.util.List;

import ro.sapientia2015.pi.dto.PIDTO;
import ro.sapientia2015.pi.model.PI;
import ro.sapientia2015.story.exception.NotFoundException;

public interface PIService {
	public PI add(PIDTO added);

    public PI deleteById(Long id) throws NotFoundException;

    public List<PI> findAll();

    public PI findById(Long id) throws NotFoundException;

    public PI update(PIDTO updated) throws NotFoundException;
}
