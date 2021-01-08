package ro.sapientia2015.story.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Kiss Tibor
 */
public class RepositoryStoryServiceTest {

    private RepositoryStoryService service;

    private StoryRepository repositoryMock;
    
    private PeriodFormatter formatter;

    @Before
    public void setUp() {
    	formatter = new PeriodFormatterBuilder()
      		     .appendDays()
      		     .appendSuffix("d")
      		     .appendHours()
      		     .appendSuffix("h")
      		     .appendMinutes()
      		     .appendSuffix("m")
      		     .appendSeconds()
      		     .appendSuffix("s")
      		     .toFormatter();
    	
        service = new RepositoryStoryService();

        repositoryMock = mock(StoryRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        StoryDTO dto = StoryTestUtil.createFormObject(null, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE, StoryTestUtil.STATUS);

        service.add(dto);

        ArgumentCaptor<Story> storyArgument = ArgumentCaptor.forClass(Story.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Story model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
        assertEquals(dto.getStatus(), model.getStatus());
        assertEquals(dto.getDueDate(), model.getDueDateAsDateTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), model.getTodoStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), model.getProgressStatusTime());
        assertEquals(formatter.print(Duration.ZERO.toPeriod()), model.getTestingStatusTime());
    }

    @Test
    public void deleteById() throws NotFoundException {
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(model);

        Story actual = service.deleteById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
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
    }

    @Test
    public void findAll() {
        List<Story> models = new ArrayList<Story>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Story> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(model);

        Story actual = service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(null);

        service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED, StoryTestUtil.DUEDATE, StoryTestUtil.STATUS_UPDATED_1);
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.DUEDATE);
        DateTime creationTIme = model.getModificationTime();
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Story actual = service.update(dto);
        DateTime updateTime = actual.getModificationTime();

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
        assertEquals(dto.getStatus(), actual.getStatus());
        assertEquals(formatter.print(new Period(creationTIme,updateTime)), actual.getTodoStatusTime());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED,StoryTestUtil.DUEDATE, StoryTestUtil.STATUS_UPDATED_1);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
