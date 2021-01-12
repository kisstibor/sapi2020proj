package ro.sapientia2015.story.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        service = new RepositoryCommentService();

        repositoryMock = mock(CommentRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);

        storyRepositoryMock = mock(StoryRepository.class);
        ReflectionTestUtils.setField(service, "storyRepository", storyRepositoryMock);
    }

    @Test
    public void add() throws NotFoundException {
        CommentDTO dto = CommentTestUtil.createFormObject(null, CommentTestUtil.MESSAGE, CommentTestUtil.STORY_ID);
        when(storyRepositoryMock.findOne(CommentTestUtil.STORY_ID)).thenReturn(CommentTestUtil.STORY);

        service.add(dto);

        ArgumentCaptor<Comment> commentArgument = ArgumentCaptor.forClass(Comment.class);
        verify(repositoryMock, times(1)).save(commentArgument.capture());
        verify(storyRepositoryMock, times(1)).findOne(CommentTestUtil.STORY_ID);

        verifyNoMoreInteractions(repositoryMock);
        verifyNoMoreInteractions(storyRepositoryMock);

        Comment model = commentArgument.getValue();
        Story story = model.getStory();
        assertNull(model.getId());
        assertEquals(dto.getMessage(), model.getMessage());
        assertEquals(dto.getStoryId(), model.getStory().getId());
        assertEquals(CommentTestUtil.STORY, model.getStory());
    }

    @Test
    public void deleteById() throws NotFoundException {
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE, CommentTestUtil.STORY);
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(model);

        Comment actual = service.deleteById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(null);

        service.deleteById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
        verifyNoMoreInteractions(storyRepositoryMock);
    }

    @Test
    public void findById() throws NotFoundException {
    	Comment model = CommentTestUtil.createModel(CommentTestUtil.ID, CommentTestUtil.MESSAGE, CommentTestUtil.STORY);
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(model);

        Comment actual = service.findById(CommentTestUtil.ID);
        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
        verifyNoMoreInteractions(storyRepositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(CommentTestUtil.ID)).thenReturn(null);

        service.findById(CommentTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(CommentTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
        verifyNoMoreInteractions(storyRepositoryMock);
    }


}
