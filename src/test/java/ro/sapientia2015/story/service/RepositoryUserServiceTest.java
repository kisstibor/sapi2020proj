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

import ro.sapientia2015.story.UserTestUtil;
import ro.sapientia2015.story.dto.UserDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.User;
import ro.sapientia2015.story.repository.UserRepository;

public class RepositoryUserServiceTest {
	
	private RepositoryUserService service;

    private UserRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryUserService();

        repositoryMock = mock(UserRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        UserDTO dto = UserTestUtil.createFormObject(null, UserTestUtil.USERNAME, UserTestUtil.PASSWORD);

        service.add(dto);

        ArgumentCaptor<User> storyArgument = ArgumentCaptor.forClass(User.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        User model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getUsername(), model.getUsername());
        assertEquals(dto.getPassword(), model.getPassword());
    }
    
    @Test
    public void findAll() {
        List<User> models = new ArrayList<User>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<User> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

}
