package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author Kiss Tibor
 */
@Service
public class RepositoryUserService implements UserService {

    @Resource
    private UserRepository repository;

    @Transactional
    @Override
    public User add(UserDTO added) {

        User model = User.getBuilder(added.getName())
                .type(added.getType())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public User deleteById(Long id) throws NotFoundException {
        User deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public User findById(Long id) throws NotFoundException {
        User found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public User update(UserDTO updated) throws NotFoundException {
    	
        User model = findById(updated.getId());
        model.update(updated.getType(), updated.getName());

        return model;
    }
}
