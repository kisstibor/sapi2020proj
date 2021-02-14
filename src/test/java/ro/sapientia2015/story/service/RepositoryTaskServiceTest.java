package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.TaskTestUtil;
import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Task;
import ro.sapientia2015.story.repository.TaskRepository;
import ro.sapientia2015.story.service.RepositoryTaskService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RepositoryTaskServiceTest {

    private RepositoryTaskService service;

    private TaskRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryTaskService();

        repositoryMock = mock(TaskRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        TaskDTO dto = TaskTestUtil.createFormObject(null, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);

        service.add(dto);

        ArgumentCaptor<Task> TaskArgument = ArgumentCaptor.forClass(Task.class);
        verify(repositoryMock, times(1)).save(TaskArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Task model = TaskArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void deleteById() throws NotFoundException {
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(repositoryMock.findOne(TaskTestUtil.ID)).thenReturn(model);

        Task actual = service.deleteById(TaskTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(TaskTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(TaskTestUtil.ID)).thenReturn(null);

        service.deleteById(TaskTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(TaskTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Task> models = new ArrayList<Task>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Task> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(repositoryMock.findOne(TaskTestUtil.ID)).thenReturn(model);

        Task actual = service.findById(TaskTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(TaskTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(TaskTestUtil.ID)).thenReturn(null);

        service.findById(TaskTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(TaskTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        TaskDTO dto = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION_UPDATED, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.PRIORITY_UPDATED);
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION, TaskTestUtil.TITLE, TaskTestUtil.PRIORITY);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Task actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        TaskDTO dto = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.DESCRIPTION_UPDATED, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.PRIORITY_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}