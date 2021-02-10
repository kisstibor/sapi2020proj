package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.repository.BugRepository;


@Service
public class RepositoryBugService implements BugService {

	@Resource
    private BugRepository repository;

    @Transactional
    @Override
    public Bug add(BugDTO added) {

        Bug model = Bug.getBuilder(added.getTitle())
                .description(added.getDescription())
                .status(added.getStatus())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Bug deleteById(Long id) throws NotFoundException {
        Bug deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Bug> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Bug findById(Long id) throws NotFoundException {
        Bug found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Bug update(BugDTO updated) throws NotFoundException {
        Bug model = findById(updated.getId());
        model.update(updated.getTitle(), updated.getDescription(), updated.getStatus());

        return model;
    }

}