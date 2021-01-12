package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.project.dto.EpicDTO;
import ro.sapientia2015.project.model.Epic;
import ro.sapientia2015.project.repository.EpicRepository;
import ro.sapientia2015.project.service.RepositoryEpicService;
import ro.sapientia2015.story.EpicTestUtil;
import ro.sapientia2015.story.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;



public class RepositoryEpicServiceTest {

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

        ArgumentCaptor<Epic> EpicArgument = ArgumentCaptor.forClass(Epic.class);
        verify(repositoryMock, times(1)).save(EpicArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Epic model = EpicArgument.getValue();

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

}
