package ro.sapientia2015.story.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryTimeLimitDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryTimeLimit;
import ro.sapientia2015.story.repository.StoryTimeLimitRepository;

public class RepositoryStoryTimeLimitServiceTest {
	private RepositoryStoryTimeLimitService service;

    private StoryTimeLimitRepository repositoryMock;
    
    private static final Long ID = 1L;
	private static final Long STORY_ID = 1L;
	private static final Long STORY_ID_UPDATED = 2L;
    private static final String TIMELIMIT = "2020-02-02";
    private static final String TIMELIMIT_UPDATED = "2020-12-12";

    public static StoryTimeLimitDTO createFormObject(Long id, String timelimit, Long storyId) {
        StoryTimeLimitDTO dto = new StoryTimeLimitDTO();

        dto.setId(id);
        dto.setTimelimit(timelimit);
        dto.setStoryId(storyId);

        return dto;
    }

    public static StoryTimeLimit createModel(Long id, String timelimit, Long storyId) {
        StoryTimeLimit model = StoryTimeLimit.getBuilder(storyId)
                .timelimit(timelimit)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    @Before
    public void setUp() {
        service = new RepositoryStoryTimeLimitService();

        repositoryMock = mock(StoryTimeLimitRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        StoryTimeLimitDTO dto = createFormObject(null,TIMELIMIT, STORY_ID);

        service.add(dto);

        ArgumentCaptor<StoryTimeLimit> storytimelimitArgument = ArgumentCaptor.forClass(StoryTimeLimit.class);
        verify(repositoryMock, times(1)).save(storytimelimitArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        StoryTimeLimit model = storytimelimitArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getTimelimit(), model.getTimelimit());
        assertEquals(dto.getStoryId(), model.getStoryId());
    }

    @Test
    public void deleteById() throws NotFoundException {
        StoryTimeLimit model = createModel(ID, TIMELIMIT, STORY_ID);
        when(repositoryMock.findOne(ID)).thenReturn(model);

        StoryTimeLimit actual = service.deleteById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(ID)).thenReturn(null);

        service.deleteById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findById() throws NotFoundException {
        StoryTimeLimit model = createModel(ID, TIMELIMIT, STORY_ID);
        when(repositoryMock.findOne(ID)).thenReturn(model);

        StoryTimeLimit actual = service.findById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(ID)).thenReturn(null);

        service.findById(ID);

        verify(repositoryMock, times(1)).findOne(ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryTimeLimitDTO dto = createFormObject(ID, TIMELIMIT_UPDATED, STORY_ID_UPDATED);
        StoryTimeLimit model = createModel(ID, TIMELIMIT, STORY_ID);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        StoryTimeLimit actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getTimelimit(), actual.getTimelimit());
        
        assertNotSame(dto.getStoryId(), actual.getStoryId());
        assertEquals(model.getStoryId(), actual.getStoryId());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryTimeLimitDTO dto = createFormObject(ID, TIMELIMIT_UPDATED, STORY_ID_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
