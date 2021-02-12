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
import ro.sapientia2015.story.BugTestUtil;
import ro.sapientia2015.story.controller.BugController;
import ro.sapientia2015.story.dto.BugDTO;
import ro.sapientia2015.story.model.Bug;

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
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITBugControllerTest {

	private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";
    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_STATUS = "status";

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
        mockMvc.perform(get("/bug/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/add.jsp"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/bug/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addWhenTitleDescriptionAndStatusAreTooLong() throws Exception {
        String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String status = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_STATUS + 1);

        mockMvc.perform(post("/bug/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, title)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_STATUS, status)
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "status"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", is(title))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", is(status))));
    }

    @Test
    @ExpectedDatabase(value="bugData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/bug/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_TITLE, "title")
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_STATUS, "status")
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(BugController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(BugController.FLASH_MESSAGE_KEY_FEEDBACK, is("Bug entry: title was added.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findAll() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_LIST))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/list.jsp"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE_LIST, hasSize(2)))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("Foo")),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("status", is("TO_DO"))
                        )
                )))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE_LIST, hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("title", is("Bar")),
                                hasProperty("description", is("Lorem ipsum")),
                                hasProperty("status", is("TO_DO"))
                        )
                )));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findById() throws Exception {
        mockMvc.perform(get("/bug/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_VIEW))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/view.jsp"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", is("TO_DO"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void findByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/bug/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("bugData-delete-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/bug/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(BugController.FLASH_MESSAGE_KEY_FEEDBACK, is("Bug entry: Bar was deleted.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void deleteByIdWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/bug/delete/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showUpdateForm() throws Exception {
        mockMvc.perform(get("/bug/update/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/update.jsp"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", is("Foo"))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", is("Lorem ipsum"))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", is("TO_DO"))));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showUpdateFormWhenIsNotFound() throws Exception {
        mockMvc.perform(get("/bug/update/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateEmpty() throws Exception {
        mockMvc.perform(post("/bug/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_ID, "1")
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateWhenTitleDescriptionAndStatusAreTooLong() throws Exception {
        String title = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_TITLE + 1);
        String description = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_DESCRIPTION + 1);
        String status = BugTestUtil.createStringWithLength(Bug.MAX_LENGTH_STATUS + 1);

        mockMvc.perform(post("/bug/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, title)
                .param(FORM_FIELD_STATUS, status)
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(BugController.VIEW_UPDATE))
                .andExpect(forwardedUrl("/WEB-INF/jsp/bug/update.jsp"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "title"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attributeHasFieldErrors(BugController.MODEL_ATTRIBUTE, "status"))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("id", is(1L))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("title", is(title))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("description", is(description))))
                .andExpect(model().attribute(BugController.MODEL_ATTRIBUTE, hasProperty("status", is(status))));
    }

    @Test
    @ExpectedDatabase(value="bugData-update-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        String expectedRedirectViewPath = BugTestUtil.createRedirectViewPath(BugController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/bug/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_ID, "1")
                .param(FORM_FIELD_TITLE, "title")
                .param(FORM_FIELD_STATUS, "status")
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(BugController.PARAMETER_ID, is("1")))
                .andExpect(flash().attribute(BugController.FLASH_MESSAGE_KEY_FEEDBACK, is("Bug entry: title was updated.")));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void updateWhenIsNotFound() throws Exception {
        mockMvc.perform(post("/bug/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "description")
                .param(FORM_FIELD_ID, "3")
                .param(FORM_FIELD_TITLE, "title")
                .param(FORM_FIELD_STATUS, "status")
                .sessionAttr(BugController.MODEL_ATTRIBUTE, new BugDTO())
        )
                .andExpect(status().isNotFound())
                .andExpect(view().name(ErrorController.VIEW_NOT_FOUND))
                .andExpect(forwardedUrl("/WEB-INF/jsp/error/404.jsp"));
    }
}

