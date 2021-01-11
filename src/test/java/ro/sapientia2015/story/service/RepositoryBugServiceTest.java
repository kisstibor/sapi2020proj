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

import ro.sapientia2015.story.BugTestUtil;
import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.repository.BugRepository;

public class RepositoryBugServiceTest {
	
	private RepositoryBugService service;

    private BugRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryBugService();

        repositoryMock = mock(BugRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
    	BugDTO dto = BugTestUtil.createFormObject(null, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);

        service.add(dto);

        ArgumentCaptor<Bug> bugArgument = ArgumentCaptor.forClass(Bug.class);
        verify(repositoryMock, times(1)).save(bugArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Bug model = bugArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void deleteById() throws NotFoundException {
    	Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(model);

        Bug actual = service.delete(BugTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(null);

        service.delete(BugTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Bug> models = new ArrayList<Bug>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Bug> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
    	Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(model);

        Bug actual = service.findById(BugTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(BugTestUtil.ID)).thenReturn(null);

        service.findById(BugTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(BugTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
    	BugDTO dto = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.TITLE_UPDATED);
    	Bug model = BugTestUtil.createModel(BugTestUtil.ID, BugTestUtil.DESCRIPTION, BugTestUtil.TITLE);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Bug actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	BugDTO dto = BugTestUtil.createFormObject(BugTestUtil.ID, BugTestUtil.DESCRIPTION_UPDATED, BugTestUtil.TITLE_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }

}
