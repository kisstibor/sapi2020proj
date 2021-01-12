package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.repository.UserRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Kiss Tibor
 */
public class RepositoryStoryServiceTest {

    private RepositoryStoryService service;

    private StoryRepository repositoryMock;
    
    

    @Before
    public void setUp() {
        service = new RepositoryStoryService();

        repositoryMock = mock(StoryRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        StoryDTO dto = StoryTestUtil.createFormObject(null, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);

        service.add(dto);

        ArgumentCaptor<Story> storyArgument = ArgumentCaptor.forClass(Story.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Story model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
    }
    
    @Test
    public void addStoryWithAssignedUser() {
    	StoryDTO dto = StoryTestUtil.createFormObject(null, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, StoryTestUtil.USER_ID);
    	User user = UserTestUtil.createModel(StoryTestUtil.ID, null, null);
    	
    	UserRepository userRepositoryMock = mock(UserRepository.class);
        ReflectionTestUtils.setField(service, "userRepository", userRepositoryMock);
        when(userRepositoryMock.findOne(StoryTestUtil.USER_ID)).thenReturn(user);
    	
    	service.add(dto);
    	
    	ArgumentCaptor<Story> storyArgument = ArgumentCaptor.forClass(Story.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verify(userRepositoryMock, times(1)).findOne(StoryTestUtil.USER_ID);
        verifyNoMoreInteractions(repositoryMock, userRepositoryMock);

        Story model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
        assertEquals(user, model.getUser());
    }

    @Test
    public void deleteById() throws NotFoundException {
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(model);

        Story actual = service.deleteById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(null);

        service.deleteById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Story> models = new ArrayList<Story>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Story> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void findByTitleNullText() throws NotFoundException {
    	List<Story> models = new ArrayList<Story>();
    	when(repositoryMock.findAll()).thenReturn(models);
    	
    	try {
    		service.findByTitle(null);
    	}
    	catch (NotFoundException exception) {
    		throw exception;
    	}
    	finally {
    		verifyNoMoreInteractions(repositoryMock);
    	}
    }
    
    @Test
    public void findByTitleWhenFound() throws NotFoundException {
    	List<Story> models = new ArrayList<Story>();
    	Story matching = StoryTestUtil.createModel(2l, null, "Second story");
    	models.add(StoryTestUtil.createModel(1l, null, "First story"));
    	models.add(matching);
    	when(repositoryMock.findAll()).thenReturn(models);
    	
    	List<Story> expected = Arrays.asList(matching);
    	
    	List<Story> actual = service.findByTitle("second");
    	
    	verify(repositoryMock, times(1)).findAll();
    	verifyNoMoreInteractions(repositoryMock);
    	
    	assertEquals(expected, actual);
    }
    
    @Test(expected = NotFoundException.class)
    public void findByTitleWhenNotFound() throws NotFoundException {
    	List<Story> models = new ArrayList<Story>();
    	when(repositoryMock.findAll()).thenReturn(models);

    	try {
    		service.findByTitle("third");
    	}
		catch (NotFoundException exception) {
    		throw exception;
    	}
    	finally {
	    	verify(repositoryMock, times(1)).findAll();
	    	verifyNoMoreInteractions(repositoryMock);
    	}
    }

    @Test
    public void findById() throws NotFoundException {
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE);
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(model);

        Story actual = service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(null);

        service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED, StoryTestUtil.USER_ID_UPDATED);
        User expectedUser = UserTestUtil.createModel(StoryTestUtil.USER_ID_UPDATED, null, null);
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, null);
        
        UserRepository userRepositoryMock = mock(UserRepository.class);
        ReflectionTestUtils.setField(service, "userRepository", userRepositoryMock);
        
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);
        when(userRepositoryMock.findOne(StoryTestUtil.USER_ID_UPDATED)).thenReturn(expectedUser);

        Story actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verify(userRepositoryMock, times(1)).findOne(StoryTestUtil.USER_ID_UPDATED);
        verifyNoMoreInteractions(repositoryMock, userRepositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
        assertEquals(expectedUser, actual.getUser());
    }
    
    @Test
    public void updateWithUnassign() throws NotFoundException {
        StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED, null);
        User expectedUser = null;
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, UserTestUtil.createModel(StoryTestUtil.USER_ID, null, null));
        
        UserRepository userRepositoryMock = mock(UserRepository.class);
        ReflectionTestUtils.setField(service, "userRepository", userRepositoryMock);
        
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Story actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
        verifyZeroInteractions(userRepositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
        assertEquals(expectedUser, actual.getUser());
    }
    
    @Test
    public void updateWithSameUser() throws NotFoundException {
    	StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED, StoryTestUtil.USER_ID);
        User expectedUser = UserTestUtil.createModel(StoryTestUtil.USER_ID, null, null);
        Story model = StoryTestUtil.createModel(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION, StoryTestUtil.TITLE, expectedUser);
        
        UserRepository userRepositoryMock = mock(UserRepository.class);
        ReflectionTestUtils.setField(service, "userRepository", userRepositoryMock);
        
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Story actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
        verifyZeroInteractions(userRepositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
        assertEquals(expectedUser, actual.getUser());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryDTO dto = StoryTestUtil.createFormObject(StoryTestUtil.ID, StoryTestUtil.DESCRIPTION_UPDATED, StoryTestUtil.TITLE_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
