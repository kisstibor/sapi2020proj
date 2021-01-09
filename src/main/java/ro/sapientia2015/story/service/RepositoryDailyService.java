package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.DailyDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Daily;
import ro.sapientia2015.story.repository.DailyRepository;

import javax.annotation.Resource;

import java.util.List;

@Service
public class RepositoryDailyService implements DailyService {

    @Resource
    private DailyRepository repository;

    @Transactional
    @Override
    public Daily add(DailyDTO added) {

        Daily model = Daily.getBuilder(added.getTitle())
                .description(added.getDescription())
                .datee(added.getDatee())
                .duration(added.getDuration())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Daily deleteById(Long id) throws NotFoundException {
        Daily deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Daily> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Daily findById(Long id) throws NotFoundException {
        Daily found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Daily update(DailyDTO updated) throws NotFoundException {
        Daily model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getTitle(), updated.getDatee(), updated.getDuration());

        return model;
    }
}
