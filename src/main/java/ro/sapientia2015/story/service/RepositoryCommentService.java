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

    @Transactional(rollbackFor = {NotFoundException.class})
	@Override
	public Comment add(CommentDTO added) throws NotFoundException {
		Story story = storyRepository.findOne(added.getStoryId());

		Comment model = Comment.getBuilder(added.getMessage()).story(story).build();

		return repository.save(model);

	}

	@Transactional(rollbackFor = { NotFoundException.class })
	@Override
	public Comment deleteById(Long id) throws NotFoundException {
		Comment deleted = findById(id);
		repository.delete(deleted);
		return deleted;
	}

	@Transactional(readOnly = true, rollbackFor = { NotFoundException.class })
	@Override
	public Comment findById(Long id) throws NotFoundException {
		Comment found = repository.findOne(id);
		if (found == null) {
			throw new NotFoundException("No entry found with id: " + id);
		}

		return found;
	}
}
