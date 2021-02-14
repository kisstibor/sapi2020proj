package ro.sapientia2015.story.controller;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;

import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.model.Bug;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.BugService;
import ro.sapientia2015.story.service.StoryService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class HomeControllerTest {
	
	private HomeController controller;
	
	private BugService bugServiceMock;
	private StoryService storyServiceMock;
	
	@Resource
	private Validator validator;
	
	@Before
	public void setUp() {
		controller = new HomeController();
		
		bugServiceMock = mock(BugService.class);
        ReflectionTestUtils.setField(controller, "bugService", bugServiceMock);

        storyServiceMock = mock(StoryService.class);
        ReflectionTestUtils.setField(controller, "storyService", storyServiceMock);  
	}
	
	@Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Bug>bugModels = new ArrayList<Bug>();
        when(bugServiceMock.findAll()).thenReturn(bugModels);

        List<Story> storyModels = new ArrayList<Story>();
        when(storyServiceMock.findAll()).thenReturn(storyModels);

        String view = controller.findAll(model);
        
        verify(bugServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(bugServiceMock);
        verify(storyServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(storyServiceMock);

        assertEquals(HomeController.VIEW_LIST, view);
        assertEquals(bugModels, model.asMap().get(HomeController.MODEL_ATTRIBUTE_LIST_BUG));
        assertEquals(storyModels, model.asMap().get(HomeController.MODEL_ATTRIBUTE_LIST_STORY));
    }

}
