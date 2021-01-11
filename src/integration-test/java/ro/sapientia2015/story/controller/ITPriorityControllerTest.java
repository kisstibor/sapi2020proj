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
import ro.sapientia2015.story.PriorityTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.dto.PriorityDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Priority;
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

/**
 * This test uses the annotation based application context configuration.
 * @author Kapas Krisztina
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITPriorityControllerTest {

    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_NAME = "name";

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
        mockMvc.perform(get("/priority/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(PriorityController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/priority/add.jsp"))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/priority/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(PriorityController.MODEL_ATTRIBUTE, new PriorityDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(PriorityController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/priority/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(PriorityController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("name", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addWhenNameIsTooLong() throws Exception {
        String name = PriorityTestUtil.createStringWithLength(Priority.MAX_LENGTH_NAME + 1);

        mockMvc.perform(post("/priority/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, name)
                .sessionAttr(PriorityController.MODEL_ATTRIBUTE, new PriorityDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(PriorityController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/priority/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(PriorityController.MODEL_ATTRIBUTE, "name"))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("name", is(name))));
    }

    @Test
    @ExpectedDatabase(value="priorityData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = PriorityTestUtil.createRedirectViewPath(PriorityController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/priority/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_NAME, "name")
                .sessionAttr(PriorityController.MODEL_ATTRIBUTE, new PriorityDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(PriorityController.FLASH_MESSAGE_KEY_FEEDBACK, is("Priority added")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/priority/list"))
                .andExpect(status().isOk())
                .andExpect(view().name(PriorityController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/priority/list.jsp"))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE_LIST, hasSize(3)))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("low"))
                        )
                )))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("name", is("medium"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/priority/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(PriorityController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/priority/view.jsp"))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(PriorityController.MODEL_ATTRIBUTE, hasProperty("name", is("low"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/priority/{id}", 44L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

