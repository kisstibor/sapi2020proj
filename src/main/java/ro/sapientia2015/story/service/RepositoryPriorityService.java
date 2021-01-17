package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.PriorityDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.PriorityRepository;
import ro.sapientia2015.story.repository.StoryRepository;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author Kiss Tibor
 */
@Service
public class RepositoryPriorityService implements PriorityService {

    @Resource
    private PriorityRepository repository;

    @Transactional
    @Override
    public Priority add(PriorityDTO added) {

    	Priority model = Priority.getBuilder(added.getTitle())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Priority deleteById(Long id) throws NotFoundException {
    	Priority deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Priority> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Priority findById(Long id) throws NotFoundException {
    	Priority found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Priority update(PriorityDTO updated) throws NotFoundException {
    	Priority model = findById(updated.getId());
        model.update(updated.getTitle());

        return model;
    }
}
