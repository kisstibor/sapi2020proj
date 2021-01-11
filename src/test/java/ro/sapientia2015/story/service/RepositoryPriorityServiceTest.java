package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.PriorityTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.PriorityDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.PriorityRepository;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Kapas Krisztina
 */
public class RepositoryPriorityServiceTest {

    private RepositoryPriorityService service;

    private PriorityRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryPriorityService();

        repositoryMock = mock(PriorityRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
    	PriorityDTO dto = PriorityTestUtil.createFormObject(null, PriorityTestUtil.NAME);

        service.add(dto);

        ArgumentCaptor<Priority> priorityArgument = ArgumentCaptor.forClass(Priority.class);
        verify(repositoryMock, times(1)).save(priorityArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Priority model = priorityArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getName(), model.getName());
    }

    @Test
    public void deleteById() throws NotFoundException {
    	Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(repositoryMock.findOne(PriorityTestUtil.ID)).thenReturn(model);

        Priority actual = service.deleteById(PriorityTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(PriorityTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(PriorityTestUtil.ID)).thenReturn(null);

        service.deleteById(PriorityTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(PriorityTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Priority> models = new ArrayList<Priority>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Priority> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
    	Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME );
        when(repositoryMock.findOne(PriorityTestUtil.ID)).thenReturn(model);

        Priority actual = service.findById(PriorityTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(PriorityTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(PriorityTestUtil.ID)).thenReturn(null);

        service.findById(PriorityTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(PriorityTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
		PriorityDTO dto = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, PriorityTestUtil.NAME_UPDATED);
		Priority model = PriorityTestUtil.createModel(PriorityTestUtil.ID, PriorityTestUtil.NAME);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Priority actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getName(), actual.getName());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	PriorityDTO dto = PriorityTestUtil.createFormObject(PriorityTestUtil.ID, PriorityTestUtil.NAME_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
