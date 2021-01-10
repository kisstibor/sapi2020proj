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

import ro.sapientia2015.story.EpicTestUtil;
import ro.sapientia2015.story.dto.EpicDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Epic;
import ro.sapientia2015.story.repository.EpicRepository;


/**
 * @author Hunor Szatmari
 */
public class RepositoryeEpicServiceTest {
	
    private RepositoryEpicService service;

	private EpicRepository repositoryMock;

	    @Before
	    public void setUp() {
	        service = new RepositoryEpicService();

	        repositoryMock = mock(EpicRepository.class);
	        ReflectionTestUtils.setField(service, "repository", repositoryMock);
	    }

	    @Test
	    public void add() {
	    	EpicDTO dto = EpicTestUtil.createFormObject(null, EpicTestUtil.DESCRIPTION, EpicTestUtil.TITLE);

	        service.add(dto);

	        ArgumentCaptor<Epic> epicArgument = ArgumentCaptor.forClass(Epic.class);
	        verify(repositoryMock, times(1)).save(epicArgument.capture());
	        verifyNoMoreInteractions(repositoryMock);

	        Epic model = epicArgument.getValue();

	        assertNull(model.getId());
	        assertEquals(dto.getDescription(), model.getDescription());
	        assertEquals(dto.getTitle(), model.getTitle());
	    }

	    @Test
	    public void deleteById() throws NotFoundException {
	        Epic model = EpicTestUtil.createModel(EpicTestUtil.ID, EpicTestUtil.DESCRIPTION, EpicTestUtil.TITLE);
	        when(repositoryMock.findOne(EpicTestUtil.ID)).thenReturn(model);

	        Epic actual = service.deleteById(EpicTestUtil.ID);

	        verify(repositoryMock, times(1)).findOne(EpicTestUtil.ID);
	        verify(repositoryMock, times(1)).delete(model);
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(model, actual);
	    }

	    @Test(expected = NotFoundException.class)
	    public void deleteByIdWhenIsNotFound() throws NotFoundException {
	        when(repositoryMock.findOne(EpicTestUtil.ID)).thenReturn(null);

	        service.deleteById(EpicTestUtil.ID);

	        verify(repositoryMock, times(1)).findOne(EpicTestUtil.ID);
	        verifyNoMoreInteractions(repositoryMock);
	    }

	    @Test
	    public void findAll() {
	        List<Epic> models = new ArrayList<Epic>();
	        when(repositoryMock.findAll()).thenReturn(models);

	        List<Epic> actual = service.findAll();

	        verify(repositoryMock, times(1)).findAll();
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(models, actual);
	    }

	    @Test
	    public void findById() throws NotFoundException {
	        Epic model = EpicTestUtil.createModel(EpicTestUtil.ID, EpicTestUtil.DESCRIPTION, EpicTestUtil.TITLE);
	        when(repositoryMock.findOne(EpicTestUtil.ID)).thenReturn(model);

	        Epic actual = service.findById(EpicTestUtil.ID);

	        verify(repositoryMock, times(1)).findOne(EpicTestUtil.ID);
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(model, actual);
	    }

	    @Test(expected = NotFoundException.class)
	    public void findByIdWhenIsNotFound() throws NotFoundException {
	        when(repositoryMock.findOne(EpicTestUtil.ID)).thenReturn(null);

	        service.findById(EpicTestUtil.ID);

	        verify(repositoryMock, times(1)).findOne(EpicTestUtil.ID);
	        verifyNoMoreInteractions(repositoryMock);
	    }

	    @Test
	    public void update() throws NotFoundException {
	        EpicDTO dto = EpicTestUtil.createFormObject(EpicTestUtil.ID, EpicTestUtil.DESCRIPTION_UPDATED, EpicTestUtil.TITLE_UPDATED);
	        Epic model = EpicTestUtil.createModel(EpicTestUtil.ID, EpicTestUtil.DESCRIPTION, EpicTestUtil.TITLE);
	        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

	        Epic actual = service.update(dto);

	        verify(repositoryMock, times(1)).findOne(dto.getId());
	        verifyNoMoreInteractions(repositoryMock);

	        assertEquals(dto.getId(), actual.getId());
	        assertEquals(dto.getDescription(), actual.getDescription());
	        assertEquals(dto.getTitle(), actual.getTitle());
	    }

	    @Test(expected = NotFoundException.class)
	    public void updateWhenIsNotFound() throws NotFoundException {
	        EpicDTO dto = EpicTestUtil.createFormObject(EpicTestUtil.ID, EpicTestUtil.DESCRIPTION_UPDATED, EpicTestUtil.TITLE_UPDATED);
	        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

	        service.update(dto);

	        verify(repositoryMock, times(1)).findOne(dto.getId());
	        verifyNoMoreInteractions(repositoryMock);
	    }

}
