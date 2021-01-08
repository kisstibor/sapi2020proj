package ro.sapientia2015.story.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.CommentRepository;
import ro.sapientia2015.story.repository.StoryRepository;

@Service
public class RepositoryCommentService implements CommentService {

	@Resource
    private CommentRepository repository;
	
	@Resource
    private StoryRepository storyRepository;
	
	@Transactional
	@Override
	public Comment add(CommentDTO added) {
		Comment model = Comment.getBuilder()
                .message(added.getMessage())
                .build();
		Story parent = storyRepository.findOne(added.getStoryId());
		model.setStory(parent);
		parent.addComment(model);
		Comment comment = repository.save(model);
		
        return comment;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Comment deleteById(Long id) throws NotFoundException {
		Comment deleted = findById(id);
		int index = deleted.getStory().getComments().indexOf(deleted);
		deleted.getStory().getComments().remove(index);
        repository.delete(deleted);
        return deleted;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Comment> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
	@Override
	public Comment findById(Long id) throws NotFoundException {
		Comment found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
	}

	@Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Comment update(CommentDTO updated) throws NotFoundException {
		Comment model = findById(updated.getId());
        model.update(updated.getMessage());

        return model;
	}

}
