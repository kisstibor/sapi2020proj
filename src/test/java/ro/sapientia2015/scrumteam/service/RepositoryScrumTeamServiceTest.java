package ro.sapientia2015.scrumteam.service;

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

import ro.sapientia2015.scrumteam.ScrumTeamTestUtil;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.scrumteam.dto.ScrumTeamDTO;
import ro.sapientia2015.scrumteam.model.ScrumTeam;
import ro.sapientia2015.scrumteam.repository.ScrumTeamRepository;
import ro.sapientia2015.scrumteam.service.RepositoryScrumTeamService;

public class RepositoryScrumTeamServiceTest {
	private RepositoryScrumTeamService service;

    private ScrumTeamRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryScrumTeamService();

        repositoryMock = mock(ScrumTeamRepository.class);
        //ReflectionTestUtils.setField(service, "scrumRepository", repositoryMock);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    //
    // add
    //
    
    @Test
    public void add() {
        ScrumTeamDTO dto = ScrumTeamTestUtil.createFormObject(null, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);

        service.add(dto);

        ArgumentCaptor<ScrumTeam> storyArgument = ArgumentCaptor.forClass(ScrumTeam.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        ScrumTeam model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getMembers(), model.getMembers());
        assertEquals(dto.getName(), model.getName());
    }

    //
    // delete by ID
    //
    
    @Test
    public void deleteById() throws NotFoundException {
        ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(repositoryMock.findOne(ScrumTeamTestUtil.ID)).thenReturn(model);

        ScrumTeam actual = service.deleteById(ScrumTeamTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTeamTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    //
    // Delete non existing record
    //
    
    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(ScrumTeamTestUtil.ID)).thenReturn(null);

        service.deleteById(ScrumTeamTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }
    
    //
    // Find all
    //

    @Test
    public void findAll() {
        List<ScrumTeam> models = new ArrayList<ScrumTeam>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<ScrumTeam> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }
    
    //
    // Find by ID
    //

    @Test
    public void findById() throws NotFoundException {
    	ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(repositoryMock.findOne(ScrumTeamTestUtil.ID)).thenReturn(model);

        ScrumTeam actual = service.findById(ScrumTeamTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }
    
    //
    // Find by ID (invalid ID)
    //

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(ScrumTeamTestUtil.ID)).thenReturn(null);

        service.findById(ScrumTeamTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(ScrumTeamTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    //
    // Update
    //
    
    @Test
    public void update() throws NotFoundException {
    	ScrumTeamDTO dto = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME_UPDATED, ScrumTeamTestUtil.UPDATED_MEMBERS);
        ScrumTeam model = ScrumTeamTestUtil.createModel(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME, ScrumTeamTestUtil.MEMBERS);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        ScrumTeam actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getMembers(), actual.getMembers());
        assertEquals(dto.getName(), actual.getName());
    }
    
    //
    // Update (invalid ID)
    //

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
    	ScrumTeamDTO dto = ScrumTeamTestUtil.createFormObject(ScrumTeamTestUtil.ID, ScrumTeamTestUtil.NAME_UPDATED, ScrumTeamTestUtil.UPDATED_MEMBERS);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
