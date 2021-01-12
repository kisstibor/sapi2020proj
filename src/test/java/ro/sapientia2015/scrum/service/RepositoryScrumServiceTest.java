package ro.sapientia2015.scrum.service;

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

import ro.sapientia2015.scrum.ScrumTestUtil;
import ro.sapientia2015.scrum.dto.ScrumDTO;
import ro.sapientia2015.scrum.model.Scrum;
import ro.sapientia2015.scrum.repository.ScrumRepository;
import ro.sapientia2015.story.exception.NotFoundException;

public class RepositoryScrumServiceTest {
	private RepositoryScrumService service;

    private ScrumRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryScrumService();

        repositoryMock = mock(ScrumRepository.class);
        ReflectionTestUtils.setField(service, "scrumRepository", repositoryMock);
    }

    @Test
    public void add() {
        ScrumDTO dto = ScrumTestUtil.createFormObject(null, ScrumTestUtil.MEMBERS, ScrumTestUtil.TITLE);

        service.add(dto);

        ArgumentCaptor<Scrum> scrumArgument = ArgumentCaptor.forClass(Scrum.class);
        verify(repositoryMock, times(1)).save(scrumArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Scrum model = scrumArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getMembers(), model.getMembers());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void findAll() {
        List<Scrum> models = new ArrayList<Scrum>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Scrum> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
    	Scrum model = ScrumTestUtil.createModel(ScrumTestUtil.ID, ScrumTestUtil.MEMBERS, ScrumTestUtil.TITLE);
        when(repositoryMock.findOne(ScrumTestUtil.ID)).thenReturn(model);

        Scrum actual = service.findById(ScrumTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(ScrumTestUtil.ID)).thenReturn(null);

        service.findById(ScrumTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
    	ScrumDTO dto = ScrumTestUtil.createFormObject(ScrumTestUtil.ID, ScrumTestUtil.MEMBERS_UPDATED, ScrumTestUtil.TITLE_UPDATED);
    	Scrum model = ScrumTestUtil.createModel(ScrumTestUtil.ID, ScrumTestUtil.MEMBERS_UPDATED, ScrumTestUtil.TITLE);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Scrum actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getMembers(), actual.getMembers());
        assertEquals(dto.getTitle(), actual.getTitle());
    }
}
