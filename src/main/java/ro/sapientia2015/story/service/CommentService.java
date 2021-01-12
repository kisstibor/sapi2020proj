package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;


public interface CommentService {


    public Comment add(CommentDTO added) throws NotFoundException;


    public Comment deleteById(Long id) throws NotFoundException;


    public Comment findById(Long id) throws NotFoundException;

}
