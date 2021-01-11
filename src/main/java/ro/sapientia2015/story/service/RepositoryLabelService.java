package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Label;
import ro.sapientia2015.story.repository.LabelRepository;

@Service
public class RepositoryLabelService implements LabelService {


    @Resource
    private LabelRepository repository;

    @Transactional
    @Override
    public Label add(LabelDTO added) {
    	Label model = added.getBuilder().setTitle(added.getTitle())
                .build();

        return repository.save(model);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Label> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Label findById(Long id){
    	Label found = repository.findOne(id);

        return found;
    }
}
