package ro.sapientia2015.task.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.task.TaskTestUtil;
import ro.sapientia2015.task.controller.TaskController;
import ro.sapientia2015.task.dto.TaskDTO;
import ro.sapientia2015.task.model.Task;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Story;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("taskData.xml")
public class ITTaskControllerTest {

    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("taskData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/story/{storyId}"))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/story/view.jsp"))
                // model attribute:  actual story
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
                //model attribute: new task 
                .andExpect(model().attribute("new_task", hasProperty("id", nullValue())))
                .andExpect(model().attribute("new_task", hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("taskData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/story/{storyId}/add_task")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr("new_task", new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.REQUEST_MAPPING_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/story/view.jsp"))
                .andExpect(model().attributeHasFieldErrors("new_task", "title"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_TASK, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_TASK, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("taskData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);
        

        mockMvc.perform(post("/story/{storyId}/add_task")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(TaskController.MODEL_ATTRIBUTE_TASK, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.REQUEST_MAPPING_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/story/view.jsp"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE_TASK, "title"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_TASK, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_TASK, hasProperty("title", is(title))));
    }

    @Test
    @ExpectedDatabase(value="taskData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/story/{storyId}/add_task")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, "title")
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(TaskController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(TaskController.FLASH_MESSAGE_KEY_FEEDBACK, is("Task entry: title was added.")));
    }
/*
    @Test
    @ExpectedDatabase("storyData.xml")
    public void findAll(Long storyId) throws Exception {
        mockMvc.perform(get("/story/{storyId}/"))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/story/" + storyId + "/"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(StoryController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Foo"))
                        )
                )))
                .andExpect(model().attribute(StoryController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Bar"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/story/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(StoryController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/story/view.jsp"))
                .andExpect(model().attribute(StoryController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(StoryController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(StoryController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/story/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
*/
    @Test
    @ExpectedDatabase("storyData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_VIEW);
        mockMvc.perform(get("/task/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(TaskController.FLASH_MESSAGE_KEY_FEEDBACK, is("Task entry: Bar was deleted.")));
    }

    @Test
    @ExpectedDatabase("taskData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/task/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

