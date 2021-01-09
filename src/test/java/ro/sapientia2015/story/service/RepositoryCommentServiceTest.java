package ro.sapientia2015.story.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.CommentTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.CommentDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Comment;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.CommentRepository;
import ro.sapientia2015.story.repository.StoryRepository;

public class RepositoryCommentServiceTest {


    private RepositoryCommentService service;

    private CommentRepository repositoryMock;
    
    private StoryRepository storyRepositoryMock;
    
    private StoryRepository storyRepository;
    

    @Before
    public void setUp() {
        service = new RepositoryCommentService();

        repositoryMock = mock(CommentRepository.class);
        storyRepositoryMock = mock(StoryRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
        ReflectionTestUtils.setField(service, "storyRepository", storyRepositoryMock);
        
        Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        when(storyRepositoryMock.findOne(StoryTestUtil.ID)).thenReturn(modelStory);
    }

    @Test
    public void add() {
    	CommentDTO dto = CommentTestUtil.createFormObject(null, CommentTestUtil.MESSAGE);
    	dto.setStoryId(StoryTestUtil.ID);
        service.add(dto);

        ArgumentCaptor<Comment> storyArgument = ArgumentCaptor.forClass(Comment.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Comment model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getMessage(), model.getMessage());
        assertEquals(model.getStory().getComments().size(), 1);
    }

    @Test
    public void deleteById() throws NotFoundException {
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
    	Story modelStory = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
    	model.setStory(modelStory);
    	modelStory.addComment(model);
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(model);

        Comment actual = service.deleteById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
        assertEquals(modelStory.getComments().size(), 0);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(null);

        service.deleteById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Comment> models = new ArrayList<Comment>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Comment> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID,CommentTestUtil.MESSAGE);
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(model);

        Comment actual = service.findById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(null);

        service.findById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
    	CommentDTO dto = CommentTestUtil.createFormObject(CommentTestUtil.ID, CommentTestUtil.MESSAGE_UPDATED);
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Comment actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getMessage(), actual.getMessage());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	CommentDTO dto = CommentTestUtil.createFormObject(CommentTestUtil.ID, CommentTestUtil.MESSAGE_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
	
}
