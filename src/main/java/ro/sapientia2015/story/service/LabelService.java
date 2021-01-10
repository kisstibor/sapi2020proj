package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Label;
import ro.sapientia2015.story.repository.LabelRepository;

public class LabelService {

    @Resource
    private LabelRepository repository;

    @Transactional
    public Label add(LabelDTO added) {

    	Label model = added.getBuilder().setTitle(added.getTitle())
                .build();

        return repository.save(model);
    }
    
    @Transactional(readOnly = true)
    public List<Label> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    public Label findById(Long id) throws NotFoundException {
    	Label found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

}
