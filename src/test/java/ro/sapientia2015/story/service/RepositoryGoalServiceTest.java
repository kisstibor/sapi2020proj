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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.GoalTestUtil;
import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Goal;
import ro.sapientia2015.story.repository.GoalRepository;

public class RepositoryGoalServiceTest {
	
	private RepositoryGoalService service;
	private GoalRepository repositoryMock;
	
    @Before
    public void setUp() {
        service = new RepositoryGoalService();

        repositoryMock = mock(GoalRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }
    
    @Test
    public void add() {
        GoalDTO dto = GoalTestUtil.createFormObject(null, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);

        service.add(dto);

        ArgumentCaptor<Goal> goalArgument = ArgumentCaptor.forClass(Goal.class);
        verify(repositoryMock, times(1)).save(goalArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Goal model = goalArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getGoal(), model.getGoal());
        assertEquals(dto.getMethod(), model.getMethod());
        assertEquals(dto.getMetrics(), model.getMetrics());
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        Goal model = GoalTestUtil.createModel(GoalTestUtil.ID, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);
        when(repositoryMock.findOne(GoalTestUtil.ID)).thenReturn(model);

        Goal actual = service.deleteById(GoalTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(GoalTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(GoalTestUtil.ID)).thenReturn(null);

        service.deleteById(GoalTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(GoalTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
    @Test
    public void findAll() {
        List<Goal> models = new ArrayList<Goal>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Goal> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }
    
    @Test
    public void findById() throws NotFoundException {
    	Goal model = GoalTestUtil.createModel(GoalTestUtil.ID, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);
        when(repositoryMock.findOne(GoalTestUtil.ID)).thenReturn(model);

        Goal actual = service.findById(GoalTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(GoalTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(GoalTestUtil.ID)).thenReturn(null);

        service.findById(GoalTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(GoalTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
    @Test
    public void update() throws NotFoundException {
    	GoalDTO dto = GoalTestUtil.createFormObject(GoalTestUtil.ID, GoalTestUtil.GOAL, GoalTestUtil.METHOD_UPDATED, GoalTestUtil.METRICS_UPDATED);
    	Goal model = GoalTestUtil.createModel(GoalTestUtil.ID, GoalTestUtil.GOAL, GoalTestUtil.METHOD, GoalTestUtil.METRICS);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Goal actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getGoal(), actual.getGoal());
        assertEquals(dto.getMethod(), actual.getMethod());
        assertEquals(dto.getMetrics(), actual.getMetrics());
    }
    
    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	GoalDTO dto = GoalTestUtil.createFormObject(null, GoalTestUtil.GOAL, GoalTestUtil.METHOD_UPDATED, GoalTestUtil.METRICS_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
