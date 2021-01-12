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

import ro.sapientia2015.story.FixVersionTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.FixVersionDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.FixVersion;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.FixVersionRepository;

public class RepositoryFixVersionTest {

	private RepositoryFixVersionService service;

    private FixVersionRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryFixVersionService();

        repositoryMock = mock(FixVersionRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }
    
    @Test
    public void add() {
        FixVersionDTO dto = FixVersionTestUtil.createFormObject(null, FixVersionTestUtil.NAME);

        service.add(dto);

        ArgumentCaptor<FixVersion> fixVersionArgument = ArgumentCaptor.forClass(FixVersion.class);
        verify(repositoryMock, times(1)).save(fixVersionArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        FixVersion model = fixVersionArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getName(), model.getName());
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(repositoryMock.findOne(FixVersionTestUtil.ID)).thenReturn(model);

        FixVersion actual = service.deleteById(FixVersionTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(FixVersionTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(FixVersionTestUtil.ID)).thenReturn(null);

        service.deleteById(FixVersionTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
    @Test
    public void findAll() {
        List<FixVersion> models = new ArrayList<FixVersion>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<FixVersion> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }
    
    @Test
    public void findById() throws NotFoundException {
        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(repositoryMock.findOne(FixVersionTestUtil.ID)).thenReturn(model);

        FixVersion actual = service.findById(FixVersionTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(FixVersionTestUtil.ID)).thenReturn(null);

        service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(FixVersionTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
    @Test
    public void update() throws NotFoundException {
        FixVersionDTO dto = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, FixVersionTestUtil.NAME_UPDATED);
        FixVersion model = FixVersionTestUtil.createModel(FixVersionTestUtil.ID, FixVersionTestUtil.NAME);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        FixVersion actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getName(), actual.getName());
    }
    
    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        FixVersionDTO dto = FixVersionTestUtil.createFormObject(FixVersionTestUtil.ID, FixVersionTestUtil.NAME_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
