package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.repository.UserRepository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiss Tibor
 */
@Service
public class RepositoryStoryService implements StoryService {

    @Resource
    private StoryRepository repository;
    
    @Resource
    private UserRepository userRepository;

    @Transactional
    @Override
    public Story add(StoryDTO added) {
    	User user = null;
    	if (added.getUserId() != null) {
    		user = userRepository.findOne(added.getUserId());
    	}
    	
        Story model = Story.getBuilder(added.getTitle())
                .description(added.getDescription())
                .user(user)
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story deleteById(Long id) throws NotFoundException {
        Story deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Story> findAll() {
       return repository.findAll();
    }
    
	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public List<Story> findByTitle(String text) throws NotFoundException {
		if (text == null) {
			throw new NotFoundException("No entry found for: " + text);
		} else {
			List<Story> allStories = repository.findAll();
			String lowercaseText = text.toLowerCase();

			ArrayList<Story> filtered = new ArrayList<Story>();
			for (Story item : allStories) {
				if (item.getTitle().toLowerCase().contains(lowercaseText)) {
					filtered.add(item);
				}
			}
			
			if (filtered.isEmpty()) {
				throw new NotFoundException("No entry found for: " + text);
			}
			else {
				return filtered;
			}
		}
	}

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Story findById(Long id) throws NotFoundException {
        Story found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Story update(StoryDTO updated) throws NotFoundException {
        Story model = findById(updated.getId());
        User user = model.getUser();
        if (updated.getUserId() == null) {
        	user = null;
        }
        else if (user == null || updated.getUserId() != user.getId()) {
        	user = userRepository.findOne(updated.getUserId());
        }
        model.update(updated.getDescription(), updated.getTitle(), user);

        return model;
    }
}
