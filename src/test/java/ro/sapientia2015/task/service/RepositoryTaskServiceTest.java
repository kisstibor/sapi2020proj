package ro.sapientia2015.task.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
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

import ro.sapientia2015.task.TaskTestUtil;
import ro.sapientia2015.task.dto.TaskDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.task.model.Task;
import ro.sapientia2015.task.repository.TaskRepository;
import ro.sapientia2015.task.service.RepositoryTaskService;

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
        TaskDTO dto = TaskTestUtil.createFormObject(null, TaskTestUtil.TITLE, TaskTestUtil.story);

        service.add(dto);

        ArgumentCaptor<Task> taskArgument = ArgumentCaptor.forClass(Task.class);
        verify(repositoryMock, times(1)).save(taskArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Task model = taskArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getStory(), model.getStory());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void deleteById() throws NotFoundException {
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.TITLE, TaskTestUtil.story);
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
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.TITLE, TaskTestUtil.story);
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
        TaskDTO dto = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.story_updated);
        Task model = TaskTestUtil.createModel(TaskTestUtil.ID, TaskTestUtil.TITLE, TaskTestUtil.story );
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Task actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getStory(), actual.getStory());
        assertEquals(dto.getTitle(), actual.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        TaskDTO dto = TaskTestUtil.createFormObject(TaskTestUtil.ID, TaskTestUtil.TITLE_UPDATED, TaskTestUtil.story_updated);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
