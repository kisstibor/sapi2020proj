package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Log;
import ro.sapientia2015.story.repository.LogRepository;

@Service
public class RepositoryLogService implements LogService{

	@Resource
    private LogRepository repository;

    @Transactional
    @Override
    public Log add(LogDTO added) {
        Log model = Log.getBuilder(added.getTitle())
                .description(added.getDescription())
                .assignTo(added.getAssignTo(), added.getStatus())
                .doc(added.getDoc())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Log deleteById(Long id) throws NotFoundException {
        Log deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Log> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Log findById(Long id) throws NotFoundException {
        Log found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Log update(LogDTO updated) throws NotFoundException {
        Log model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getTitle(),updated.getAssignTo(),updated.getStatus());

        return model;
    }
}

