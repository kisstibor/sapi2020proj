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

import ro.sapientia2015.story.LogTestUtil;
import ro.sapientia2015.story.dto.LogDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Log;
import ro.sapientia2015.story.repository.LogRepository;

public class RepositoryLogServiceTest {


    private RepositoryLogService service;

    private LogRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryLogService();

        repositoryMock = mock(LogRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        LogDTO dto = LogTestUtil.createFormObject(null, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE, LogTestUtil.STATUS, LogTestUtil.ASSIGNTO,LogTestUtil.DOC);

        service.add(dto);

        ArgumentCaptor<Log> logArgument = ArgumentCaptor.forClass(Log.class);
        verify(repositoryMock, times(1)).save(logArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Log model = logArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
        assertEquals(dto.getAssignTo(), model.getAssignTo());
        assertEquals(dto.getStatus(), model.getStatus());
        
    }

    @Test
    public void deleteById() throws NotFoundException {
        Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE, LogTestUtil.ASSIGNTO, LogTestUtil.STATUS, LogTestUtil.DOC);
        when(repositoryMock.findOne(LogTestUtil.ID)).thenReturn(model);

        Log actual = service.deleteById(LogTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(LogTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(LogTestUtil.ID)).thenReturn(null);

        service.deleteById(LogTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(LogTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Log> models = new ArrayList<Log>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Log> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
    	Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.DESCRIPTION, LogTestUtil.TITLE,LogTestUtil.ASSIGNTO,LogTestUtil.STATUS,LogTestUtil.DOC);
        when(repositoryMock.findOne(LogTestUtil.ID)).thenReturn(model);

        Log actual = service.findById(LogTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(LogTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(LogTestUtil.ID)).thenReturn(null);

        service.findById(LogTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(LogTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        LogDTO dto = LogTestUtil.createFormObject(LogTestUtil.ID, LogTestUtil.TITLE_UPDATED, LogTestUtil.DESCRIPTION_UPDATED, LogTestUtil.ASSINGTO_UPDATED, LogTestUtil.STATUS_UPDATED,LogTestUtil.DOC_UPDATED);
        Log model = LogTestUtil.createModel(LogTestUtil.ID, LogTestUtil.TITLE_UPDATED, LogTestUtil.DESCRIPTION_UPDATED, LogTestUtil.ASSINGTO_UPDATED, LogTestUtil.STATUS_UPDATED,LogTestUtil.DOC_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Log actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
        assertEquals(dto.getStatus(), actual.getStatus());
        assertEquals(dto.getAssignTo(), actual.getAssignTo());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	LogDTO dto = LogTestUtil.createFormObject(LogTestUtil.ID, LogTestUtil.TITLE_UPDATED, LogTestUtil.DESCRIPTION_UPDATED, LogTestUtil.ASSINGTO_UPDATED, LogTestUtil.STATUS_UPDATED,LogTestUtil.DOC_UPDATED);
        
    	when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
