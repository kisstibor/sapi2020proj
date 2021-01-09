package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.TaskController;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Task;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.TaskService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class HomeControllerTest {

    private HomeController homeController;
    
    private TaskService taskServiceMock;
    private StoryService storyServiceMock;
    
    @Resource
    private Validator validator;

    @Before
    public void setUp() {

        homeController = new HomeController();
        
        taskServiceMock = mock(TaskService.class);
        ReflectionTestUtils.setField(homeController, "taskService", taskServiceMock);
        
        storyServiceMock = mock(StoryService.class);
        ReflectionTestUtils.setField(homeController, "storyService", storyServiceMock);      
    }


    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Task> taskModels = new ArrayList<Task>();
        when(taskServiceMock.findAll()).thenReturn(taskModels);
        
        List<Story> storyModels = new ArrayList<Story>();
        when(storyServiceMock.findAll()).thenReturn(storyModels);
        
        String view = homeController.findAll(model);

        assertEquals(HomeController.VIEW_LIST, view);
        assertEquals(taskModels, model.asMap().get(HomeController.MODEL_ATTRIBUTE_LIST_TASK));
        assertEquals(storyModels, model.asMap().get(HomeController.MODEL_ATTRIBUTE_LIST_STORY));
    }
}
