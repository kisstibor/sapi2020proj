package ro.sapientia2015.story.controller;

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
import ro.sapientia2015.story.TaskTestUtil;
import ro.sapientia2015.story.controller.TaskController;
import ro.sapientia2015.story.dto.TaskDTO;
import ro.sapientia2015.story.model.Task;

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
@DatabaseSetup("storyData.xml")
public class ITTaskControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
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
    @ExpectedDatabase("storyData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/task/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/add.jsp"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/task/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);
        String description = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/task/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }

    @Test
    @ExpectedDatabase(value="taskData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_LIST);

        mockMvc.perform(post("/task/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "Lorem ipsum")
                .param(FORM_FIELD_TITLE, "Task3")
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(TaskController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(TaskController.FLASH_MESSAGE_KEY_FEEDBACK, is("Task entry: Task3 was added.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/list.jsp"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("title", is("Task1"))
                        )
                )));              
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/view.jsp"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", is("Task1"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/task/{id}", 7L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("taskData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/task/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(TaskController.FLASH_MESSAGE_KEY_FEEDBACK, is("Task entry: Task2 was deleted.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/task/delete/{id}", 7L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/task/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/update.jsp"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", is("Task1"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/task/update/{id}", 7L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/task/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateWhenTitleAndDescriptionAreTooLong() throws Exception {
        String title = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_TITLE + 1);
        String description = TaskTestUtil.createStringWithLength(Task.MAX_LENGTH_DESCRIPTION + 1);

        mockMvc.perform(post("/task/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, title)
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(TaskController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(TaskController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(TaskController.MODEL_ATTRIBUTE, hasProperty("title", is(title))));
    }

    @Test
    @ExpectedDatabase(value="taskData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = TaskTestUtil.createRedirectViewPath(TaskController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/task/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "Lorem ipsum")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, "Task4")
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(TaskController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(TaskController.FLASH_MESSAGE_KEY_FEEDBACK, is("Task entry: Task4 was updated.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/task/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description3")
                .param(FORM_FIELD_ID, "7")
                .param(FORM_FIELD_TITLE, "title4")
                .sessionAttr(TaskController.MODEL_ATTRIBUTE, new TaskDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

