package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;

public interface CommentService {

    public Comment add(CommentDTO added);

    public Comment deleteById(Long id) throws NotFoundException;

    public List<Comment> findAll();

    public Comment findById(Long id) throws NotFoundException;

    public Comment update(CommentDTO updated) throws NotFoundException;
}
